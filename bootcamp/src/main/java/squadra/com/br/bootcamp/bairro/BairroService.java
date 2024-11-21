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
            if(codigoBairro != null && codigoMunicipio == null && (nome == null || nome.isEmpty()) && status == null && !bairrosFiltrados.isEmpty()) {
                return bairrosFiltrados.getFirst();
            }
            return bairrosFiltrados;

        }catch(RuntimeException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível consultar o bairro no banco de dados");
        }
    }

    @Transactional
    public List<BairroVo> save(BairroVo bairro){
        try{
            municipioService.verificaExisteMunicipio(bairro.getCodigoMunicipio());
            verificaExisteBairroComMesmoNomeNoMunicipio(bairro);
            bairroRepository.save(bairro);

            return filtrarBairrosEOrdenarPorCodigoBairro(null, null, null, null);

        }catch (ExcecaoPersonalizadaException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar o bairro. " + ex.getMessage());

        }catch(RuntimeException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar o bairro. ");
        }
    }

    @Transactional
    public List<BairroVo> update(BairroVo bairro){
        try{
            verificaCodigoBairroNulo(bairro.getCodigoBairro());
            verificaExisteBairro(bairro.getCodigoBairro());
            municipioService.verificaExisteMunicipio(bairro.getCodigoMunicipio());
            verificaExisteBairroComMesmoNomeNoMunicipio(bairro);

            Optional<BairroVo> bairroAntigo = bairroRepository.findById(bairro.getCodigoBairro());
            if(bairroAntigo.isPresent()){
                bairroAntigo.get().setCodigoMunicipio(bairro.getCodigoMunicipio());
                bairroAntigo.get().setNome(bairro.getNome());
                bairroAntigo.get().setStatus(bairro.getStatus());
                bairroRepository.save(bairroAntigo.get());
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

    private void verificaExisteBairroComMesmoNomeNoMunicipio(BairroVo bairro) throws RegistroJaExisteNoBancoException {
        boolean existeBairroComMesmoNome =  bairroRepository.findAll().stream()
                .anyMatch(bairroDoBanco ->
                bairroDoBanco.getNome().equalsIgnoreCase(bairro.getNome()) &&
                bairroDoBanco.getCodigoMunicipio().equals(bairro.getCodigoMunicipio()) &&
                !bairroDoBanco.getCodigoBairro().equals(bairro.getCodigoBairro()));

        if(existeBairroComMesmoNome){
            throw new RegistroJaExisteNoBancoException("Já existe um bairro com nome '" + bairro.getNome() + "' no municipio " + bairro.getCodigoMunicipio());
        }
    }

    private void verificaCodigoBairroNulo(Long codigoBairro){
        if(codigoBairro == null){
            throw new ExcecaoPersonalizadaException("O campo codigoBairro não pode ser nulo");
        }
    }
    private void verificaExisteBairro(Long codigoBairro) throws RegistroNaoExisteNoBancoException {
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

    public BairroGetResponseBody converterBairroVoParaGetResponseBody(BairroVo bairro){
        return bairroMapper.toGetResponseBody(bairro);
    }
}
