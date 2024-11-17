package squadra.com.br.bootcamp.municipio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;
import squadra.com.br.bootcamp.exception.MunicipioExistenteNaUF;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        if(existeMunicipioComMesmoNomeNaUf(municipio.getNome(), municipio.getCodigoUF())){
            throw new MunicipioExistenteNaUF("Não foi possível cadastrar o município " + municipio.getNome() + " na UF de código " + municipio.getCodigoUF() + " já existe um município com este nome na UF");
        }
        try{
            municipioRepository.save(municipio);
            return filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(null, null, null, null);

        }catch(RuntimeException e){
            System.out.println(e.getMessage());
            throw new ExcecaoPersonalizada("Não foi possível registar o município.");
        }
    }

    public List<MunicipioVo> update(MunicipioVo municipio){
        if(municipio.getCodigoMunicipio() == null){
            throw new ExcecaoPersonalizada("O campo codigoMunicipio não pode ser nulo.");
        }

        Optional<MunicipioVo> municipioAntigo = municipioRepository.findById(municipio.getCodigoMunicipio());
        if (municipioAntigo.isEmpty()) {
            throw new ExcecaoPersonalizada("O Municipio de código " + municipio.getCodigoMunicipio() + " não foi encontrado.");
        }

        boolean mesmoMunicipio = municipioAntigo.get().getCodigoMunicipio().equals(municipio.getCodigoMunicipio()) &&
                                municipioAntigo.get().getNome().equalsIgnoreCase(municipio.getNome()) &&
                                municipioAntigo.get().getCodigoUF().equals(municipio.getCodigoUF());

        if(mesmoMunicipio || !existeMunicipioComMesmoNomeNaUf(municipio.getNome(), municipio.getCodigoUF())){
            try {
                municipioAntigo.get().setNome(municipio.getNome());
                municipioAntigo.get().setCodigoUF(municipio.getCodigoUF());
                municipioAntigo.get().setStatus(municipio.getStatus());
                municipioRepository.save(municipioAntigo.get());

            } catch (RuntimeException e) {
                throw new ExcecaoPersonalizada("Não foi possível realizar a alteração do município de códigoMunicipio " + municipio.getCodigoMunicipio());
            }
        }else{
            throw new ExcecaoPersonalizada("Já existe um município com o mesmo nome na UF " + municipio.getCodigoUF());

        }
        return filtrarMunicipiosEOrdenarPorCodigoUFCodigoMunicipio(null, null, null, null);
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

    public boolean existeMunicipioComMesmoNomeNaUf(String nomeMunicipio, Long codigoUf){
        return municipioRepository.findAll().stream().anyMatch(municipio ->
                municipio.getNome().equalsIgnoreCase(nomeMunicipio) &&
                municipio.getCodigoUF().equals(codigoUf));
    }
}
