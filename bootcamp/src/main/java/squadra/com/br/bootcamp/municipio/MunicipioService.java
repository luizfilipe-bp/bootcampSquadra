package squadra.com.br.bootcamp.municipio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizadaException;
import squadra.com.br.bootcamp.exception.RegistroJaExisteNoBancoException;
import squadra.com.br.bootcamp.exception.RegistroNaoExisteNoBancoException;
import squadra.com.br.bootcamp.uf.UfService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MunicipioService {
    private final MunicipioRepository municipioRepository;
    private final MunicipioMapper municipioMapper;
    private final UfService ufService;

    public Object findByParams(Long codigoMunicipio, Long codigoUF, String nome, Integer status){
        try {
            if (nome != null) {
                nome = nome.trim();
            }

            List<MunicipioVo> municipiosFiltrados = filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(codigoMunicipio, codigoUF, nome, status);
            if (codigoMunicipio != null && !municipiosFiltrados.isEmpty()) {
                return municipiosFiltrados.getFirst();
            }
            return municipiosFiltrados;

        }catch (RuntimeException ex){
            System.out.println(ex.getMessage());
            throw new ExcecaoPersonalizadaException("Não foi possível consultar o município no banco de dados.");
        }
    }

    @Transactional
    public List<MunicipioVo> save(MunicipioPostRequestBody municipioPostRequestBody){
        try{
            ufService.verificaNaoExisteUfCadastradaNoBanco(municipioPostRequestBody.getCodigoUF());
            verificaExisteMunicipioCadastradoComMesmoNomeNaUf(municipioPostRequestBody.getNome(), municipioPostRequestBody.getCodigoUF());

            municipioRepository.save(municipioMapper.toMunicipioVo(municipioPostRequestBody));
            return filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(null, null, null, null);

        }catch (ExcecaoPersonalizadaException e){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar o município. " + e.getMessage());
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar o município.");
        }
    }

    @Transactional
    public List<MunicipioVo> update(MunicipioPutRequestBody municipioPutRequestBody){
        try {
            ufService.verificaNaoExisteUfCadastradaNoBanco(municipioPutRequestBody.getCodigoUF());
            verificaExisteMunicipioCadastrado(municipioPutRequestBody.getCodigoMunicipio());

            Optional<MunicipioVo> municipioAntigo = municipioRepository.findById(municipioPutRequestBody.getCodigoMunicipio());
            if(municipioAntigo.isPresent()) {
                if(!municipioAntigo.get().getNome().equals(municipioPutRequestBody.getNome()) ||
                   !municipioAntigo.get().getCodigoUF().equals(municipioPutRequestBody.getCodigoUF())) {

                    verificaExisteMunicipioCadastradoComMesmoNomeNaUf(municipioPutRequestBody.getNome(), municipioPutRequestBody.getCodigoUF());
                }
                municipioRepository.save(municipioMapper.toMunicipioVo(municipioPutRequestBody));
            }

        }catch(ExcecaoPersonalizadaException ex){
          throw new ExcecaoPersonalizadaException("Não foi possível realizar a alteração do município. " + ex.getMessage());

        } catch (RuntimeException e) {
            throw new ExcecaoPersonalizadaException("Não foi possível realizar a alteração do município. ");
        }
        return filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(null, null, null, null);
    }

    private List<MunicipioVo> filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(Long codigoMunicipio, Long codigoUF, String nome, Integer status){
        return municipioRepository.findAll().stream()
                .filter(municipio -> codigoMunicipio == null || municipio.getCodigoMunicipio().equals(codigoMunicipio))
                .filter(municipio -> codigoUF == null || municipio.getCodigoUF().equals(codigoUF))
                .filter(municipio -> nome == null || nome.isEmpty() || municipio.getNome().equals(nome))
                .filter(municipio -> status == null || municipio.getStatus().equals(status))
                .sorted(Comparator.comparing(MunicipioVo::getCodigoUF).reversed()
                .thenComparing(MunicipioVo::getCodigoMunicipio, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private void verificaExisteMunicipioCadastradoComMesmoNomeNaUf(String nome, Long codigoUF) throws RegistroJaExisteNoBancoException {
        boolean municipioDeMesmoNomeExiste = municipioRepository.findAll().stream()
                .anyMatch(municipioDoBanco ->
                municipioDoBanco.getNome().equalsIgnoreCase(nome) &&
                municipioDoBanco.getCodigoUF().equals(codigoUF));

        if(municipioDeMesmoNomeExiste){
            throw new RegistroJaExisteNoBancoException("Já existe um município com o mesmo nome " + nome  + " na UF " + codigoUF);
        }
    }

    public void verificaExisteMunicipioCadastrado(Long codigoMunicipio) throws RegistroNaoExisteNoBancoException {
        if(!municipioRepository.existsById(codigoMunicipio)){
            throw new RegistroNaoExisteNoBancoException("Não existe município de codigoMunicipio " + codigoMunicipio + " banco de dados.");
        }
    }

    public MunicipioVo buscarMunicipioPorCodigoMunicipio(Long codigoMunicipio) throws RegistroNaoExisteNoBancoException {
        Optional<MunicipioVo> municipioVo =  municipioRepository.findById(codigoMunicipio);
        if(municipioVo.isPresent()){
            return municipioVo.get();
        }
        throw new RegistroNaoExisteNoBancoException("Não existe município de codigoMunicipio " + codigoMunicipio);
    }

    public MunicipioGetResponseBody converterMunicipioParaGetResponseBody(MunicipioVo municipio){
        return municipioMapper.toGetResponseBody(municipio);
    }
}
