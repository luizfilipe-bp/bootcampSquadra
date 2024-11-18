package squadra.com.br.bootcamp.bairro;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;
import squadra.com.br.bootcamp.exception.RegistroJaExisteNoBanco;
import squadra.com.br.bootcamp.municipio.MunicipioRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BairroService {
    private final BairroRepository bairroRepository;
    private final MunicipioRepository municipioRepository;

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
            throw new ExcecaoPersonalizada("Não foi possível consultar o bairro no banco de dados");
        }
    }

    @Transactional
    public List<BairroVo> save(BairroVo bairro){
        if(!municipioExiste(bairro.getCodigoMunicipio())){
            throw new DataIntegrityViolationException("Não foi possível cadastrar o bairro, não existe municipio de codigoMunicipio " + bairro.getCodigoMunicipio());
        }
        if(existeBairroComMesmoNomeNoMunicipio(bairro.getNome(), bairro.getCodigoMunicipio())){
            throw new RegistroJaExisteNoBanco("Não foi possível cadastrar o bairro " + bairro.getNome() + " no municipio de código " + bairro.getCodigoMunicipio() + " já existe um bairro com este nome no município");
        }

        try{
            bairroRepository.save(bairro);
            return filtrarBairrosEOrdenarPorCodigoBairro(null, null, null, null);
        }catch(RuntimeException ex){
            throw new ExcecaoPersonalizada("Não foi possível registar o bairro.");
        }
    }

    private List<BairroVo> filtrarBairrosEOrdenarPorCodigoBairro(Long codigoBairro, Long codigoMunicipio, String nome, Integer status){
        return bairroRepository.findAll().stream()
                .filter(bairro -> codigoBairro == null || bairro.getCodigoBairro().equals(codigoBairro))
                .filter(bairro -> codigoMunicipio == null || bairro.getCodigoMunicipio().equals(codigoMunicipio))
                .filter(bairro -> nome == null || bairro.getNome().equalsIgnoreCase(nome))
                .filter(bairro -> status == null || bairro.getStatus().equals(status))
                .sorted(Comparator.comparing(BairroVo::getCodigoMunicipio).reversed()
                .thenComparing(BairroVo::getCodigoBairro, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private boolean existeBairroComMesmoNomeNoMunicipio(String nomeBairro, Long codigoMunicipio){
        return bairroRepository.findAll().stream()
                .anyMatch(bairro ->
                bairro.getNome().equalsIgnoreCase(nomeBairro) &&
                bairro.getCodigoMunicipio().equals(codigoMunicipio));
    }

    private boolean municipioExiste(Long codigoMunicipio){
        return municipioRepository.existsById(codigoMunicipio);
    }

}
