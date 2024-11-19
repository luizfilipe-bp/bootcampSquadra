package squadra.com.br.bootcamp.municipio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;
import squadra.com.br.bootcamp.exception.RegistroJaExisteNoBanco;
import squadra.com.br.bootcamp.exception.RegistroNaoExisteNoBanco;
import squadra.com.br.bootcamp.uf.UFService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MunicipioService {
    private final MunicipioRepository municipioRepository;
    private final UFService ufService;

    public Object findByParams(Long codigoMunicipio, Long codigoUF, String nome, Integer status){
        try {
            if (nome != null) {
                nome = nome.trim();
            }

            List<MunicipioVo> municipiosFiltrados = filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(codigoMunicipio, codigoUF, nome, status);
            if (codigoMunicipio != null && codigoUF == null && (nome == null || nome.isEmpty()) && status == null && !municipiosFiltrados.isEmpty()) {
                return municipiosFiltrados.getFirst();
            }
            return municipiosFiltrados;

        }catch (RuntimeException ex){
            System.out.println(ex.getMessage());
            throw new ExcecaoPersonalizada("Não foi possível consultar o município no banco de dados.");
        }
    }

    @Transactional
    public List<MunicipioVo> save(MunicipioVo municipio){
        try{
            ufService.verificaExisteUf(municipio.getCodigoUF());
            verificaExisteMunicipioComMesmoNomeNaUf(municipio);
            municipioRepository.save(municipio);
            return filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(null, null, null, null);

        }catch (ExcecaoPersonalizada e){
            throw new ExcecaoPersonalizada("Não foi possível cadastrar o município. " + e.getMessage());
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
            throw new ExcecaoPersonalizada("Não foi possível cadastrar o município.");
        }
    }

    @Transactional
    public List<MunicipioVo> update(MunicipioVo municipio){
        try {
            verificaCodigoMunicipioNulo(municipio.getCodigoMunicipio());
            ufService.verificaExisteUf(municipio.getCodigoUF());
            verificaExisteMunicipio(municipio.getCodigoMunicipio());
            verificaExisteMunicipioComMesmoNomeNaUf(municipio);

            Optional<MunicipioVo> municipioAntigo = municipioRepository.findById(municipio.getCodigoMunicipio());
            if(municipioAntigo.isPresent()) {
                municipioAntigo.get().setNome(municipio.getNome());
                municipioAntigo.get().setCodigoUF(municipio.getCodigoUF());
                municipioAntigo.get().setStatus(municipio.getStatus());
                municipioRepository.save(municipioAntigo.get());
            }
        }catch(ExcecaoPersonalizada ex){
          throw new ExcecaoPersonalizada("Não foi possível realizar a alteração do município. " + ex.getMessage());

        } catch (RuntimeException e) {
            throw new ExcecaoPersonalizada("Não foi possível realizar a alteração do município. ");
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

    private void verificaExisteMunicipioComMesmoNomeNaUf(MunicipioVo municipio) throws RegistroJaExisteNoBanco {
        boolean municipioDeMesmoNomeExiste = municipioRepository.findAll().stream()
                .anyMatch(municipioDoBanco ->
                municipioDoBanco.getNome().equalsIgnoreCase(municipio.getNome()) &&
                municipioDoBanco.getCodigoUF().equals(municipio.getCodigoUF()) &&
                !municipioDoBanco.getCodigoMunicipio().equals(municipio.getCodigoMunicipio()));

        if(municipioDeMesmoNomeExiste){
            throw new RegistroJaExisteNoBanco("Já existe um município com o mesmo nome " + municipio.getNome()  + " na UF " + municipio.getCodigoUF());
        }
    }

    private void verificaCodigoMunicipioNulo(Long codigoMunicipio) throws ExcecaoPersonalizada{
        if(codigoMunicipio == null){
            throw new ExcecaoPersonalizada("O campo codigoMunicipio não pode ser nulo.");
        }
    }

    public void verificaExisteMunicipio(Long codigoMunicipio) throws RegistroNaoExisteNoBanco {
        if(!municipioRepository.existsById(codigoMunicipio)){
            throw new RegistroNaoExisteNoBanco("Não existe município de codigoMunicipio " + codigoMunicipio + " banco de dados.");
        }
    }
}
