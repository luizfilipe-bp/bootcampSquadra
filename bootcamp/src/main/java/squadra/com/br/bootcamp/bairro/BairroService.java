package squadra.com.br.bootcamp.bairro;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizadaException;
import squadra.com.br.bootcamp.exception.RegistroJaExisteNoBancoException;
import squadra.com.br.bootcamp.exception.RegistroNaoExisteNoBancoException;
import squadra.com.br.bootcamp.municipio.MunicipioService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BairroService {
    private final BairroRepository bairroRepository;
    private final BairroMapper bairroMapper;
    private final MunicipioService municipioService;

    public Object findByParams(Long codigoBairro, Long codigoMunicipio, String nome, Integer status){
        try{
            if(nome != null){
                nome = nome.trim();
            }
            List<BairroVo> bairrosFiltrados = filtrarBairrosEOrdenarPorCodigoBairro(codigoBairro, codigoMunicipio, nome, status);
            if(codigoBairro != null && !bairrosFiltrados.isEmpty()) {
                return bairrosFiltrados.getFirst();
            }
            return bairrosFiltrados;

        }catch(RuntimeException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível consultar o bairro no banco de dados");
        }
    }

    @Transactional
    public List<BairroVo> save(BairroPostRequestBody bairroPostRequestBody){
        try{
            municipioService.verificaExisteMunicipioCadastrado(bairroPostRequestBody.getCodigoMunicipio());
            verificaExisteBairroComMesmoNomeNoMunicipio(bairroPostRequestBody.getNome(), bairroPostRequestBody.getCodigoMunicipio());
            bairroRepository.save(bairroMapper.toBairroVo(bairroPostRequestBody));

            return filtrarBairrosEOrdenarPorCodigoBairro(null, null, null, null);

        }catch (ExcecaoPersonalizadaException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar o bairro. " + ex.getMessage());

        }catch(RuntimeException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar o bairro. ");
        }
    }

    @Transactional
    public List<BairroVo> update(BairroPutRequestBody bairroPutRequestBody){
        try{
            verificaExisteBairroCadastrado(bairroPutRequestBody.getCodigoBairro());
            municipioService.verificaExisteMunicipioCadastrado(bairroPutRequestBody.getCodigoMunicipio());

            Optional<BairroVo> bairroAntigo = bairroRepository.findById(bairroPutRequestBody.getCodigoBairro());
            if(bairroAntigo.isPresent()){
                if(!bairroAntigo.get().getNome().equals(bairroPutRequestBody.getNome()) ||
                   !bairroAntigo.get().getCodigoMunicipio().equals(bairroPutRequestBody.getCodigoMunicipio())){

                    verificaExisteBairroComMesmoNomeNoMunicipio(bairroPutRequestBody.getNome(), bairroPutRequestBody.getCodigoMunicipio());
                }
                bairroRepository.save(bairroMapper.toBairroVo(bairroPutRequestBody));
            }
            return filtrarBairrosEOrdenarPorCodigoBairro(null, null, null, null);

        }catch (ExcecaoPersonalizadaException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar o bairro. " + ex.getMessage());

        }catch(RuntimeException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar o bairro. ");
        }
    }

    private List<BairroVo> filtrarBairrosEOrdenarPorCodigoBairro(Long codigoBairro, Long codigoMunicipio, String nome, Integer status){
        return bairroRepository.findAll().stream()
                .filter(bairro -> codigoBairro == null || bairro.getCodigoBairro().equals(codigoBairro))
                .filter(bairro -> codigoMunicipio == null || bairro.getCodigoMunicipio().equals(codigoMunicipio))
                .filter(bairro -> nome == null || nome.isEmpty() || bairro.getNome().equalsIgnoreCase(nome))
                .filter(bairro -> status == null || bairro.getStatus().equals(status))
                .sorted(Comparator.comparing(BairroVo::getCodigoMunicipio).reversed()
                .thenComparing(BairroVo::getCodigoBairro, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private void verificaExisteBairroComMesmoNomeNoMunicipio(String nome, Long codigoMunicipio) throws RegistroJaExisteNoBancoException {
        boolean existeBairroComMesmoNome =  bairroRepository.findAll().stream()
                .anyMatch(bairroDoBanco ->
                bairroDoBanco.getNome().equalsIgnoreCase(nome) &&
                bairroDoBanco.getCodigoMunicipio().equals(codigoMunicipio));

        if(existeBairroComMesmoNome){
            throw new RegistroJaExisteNoBancoException("Já existe um bairro com nome '" + nome + "' no municipio " + codigoMunicipio);
        }
    }
    
    public void verificaExisteBairroCadastrado(Long codigoBairro) throws RegistroNaoExisteNoBancoException {
        if(!bairroRepository.existsById(codigoBairro)){
            throw new RegistroNaoExisteNoBancoException("O bairro de código " + codigoBairro + " não existe no banco de dados");
        }
    }

    public BairroVo buscarBairroPorCodigoBairro(Long codigoBairro) throws RegistroNaoExisteNoBancoException {
        Optional<BairroVo> bairro = bairroRepository.findById(codigoBairro);
        if(bairro.isPresent()){
            return bairro.get();
        }
        throw new RegistroNaoExisteNoBancoException("Não existe bairro de codigoBairro " + codigoBairro);
    }

    public BairroGetRequestBody converterBairroVoParaGetResponseBody(BairroVo bairro){
        return bairroMapper.toGetResponseBody(bairro);
    }
}
