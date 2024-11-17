package squadra.com.br.bootcamp.municipio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;
import squadra.com.br.bootcamp.exception.MunicipioExistenteNaUF;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MunicipioService {
    private final MunicipioRepository municipioRepository;

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

    public List<MunicipioVo> save(MunicipioVo municipio){

        try{
            existeMunicipioComMesmoNomeNaUf(municipio.getNome(), municipio.getCodigoUF());
            municipioRepository.save(municipio);
            return filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(null, null, null, null);

        }catch(MunicipioExistenteNaUF e){
            throw e;
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
            throw new ExcecaoPersonalizada("Não foi possível registar o município.");
        }
    }

    private List<MunicipioVo> filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(Long codigoMunicipio, Long codigoUF, String nome, Integer status){
        return municipioRepository.findAll().stream()
                .filter(municipio -> codigoMunicipio == null || municipio.getCodigoMunicipio().equals(codigoMunicipio))
                .filter(municipio -> codigoUF == null || municipio.getCodigoUF().equals(codigoUF))
                .filter(municipio -> nome == null || municipio.getNome().equals(nome))
                .filter(municipio -> status == null || municipio.getStatus().equals(status))
                .sorted(Comparator.comparing(MunicipioVo::getCodigoUF).reversed()
                .thenComparing(MunicipioVo::getCodigoMunicipio, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public void existeMunicipioComMesmoNomeNaUf(String nomeMunicipio, Long codigoUf) throws MunicipioExistenteNaUF{
        boolean municipioExisteNaUF =  municipioRepository.findAll().stream()
                .anyMatch(municipio ->
                        municipio.getNome().equalsIgnoreCase(nomeMunicipio) &&
                        municipio.getCodigoUF().equals(codigoUf));
        if(municipioExisteNaUF){
            throw new MunicipioExistenteNaUF("Não foi possível cadastrar o município " + nomeMunicipio + " na UF de código " + codigoUf + " já existe um município com este nome na UF");
        }
    }
}
