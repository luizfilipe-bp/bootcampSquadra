package squadra.com.br.bootcamp.municipio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;

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

            List<MunicipioVo> todosMunicipios = municipioRepository.findAll();
            List<MunicipioVo> municipiosFiltrados = filtrarMunicipios(todosMunicipios, codigoMunicipio, codigoUF, nome, status);

            if (codigoMunicipio != null && codigoUF == null && (nome == null || nome.isEmpty()) && status == null && !municipiosFiltrados.isEmpty()) {
                return municipiosFiltrados.getFirst();
            }
            return municipiosFiltrados;

        }catch (RuntimeException ex){
            System.out.println(ex.getMessage());
            throw new ExcecaoPersonalizada("Não foi possível consultar o município no banco de dados.");
        }
    }

    private List<MunicipioVo> filtrarMunicipios(List<MunicipioVo> municipios, Long codigoMunicipio, Long codigoUF, String nome, Integer status){
        return municipios.stream()
                .filter(municipio -> codigoMunicipio == null || municipio.getCodigoMunicipio().equals(codigoMunicipio))
                .filter(municipio -> codigoUF == null || municipio.getCodigoUF().equals(codigoUF))
                .filter(municipio -> nome == null || municipio.getNome().equals(nome))
                .filter(municipio -> status == null || municipio.getStatus().equals(status))
                .sorted(Comparator.comparing(MunicipioVo::getCodigoUF).reversed()
                        .thenComparing(MunicipioVo::getCodigoMunicipio, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
