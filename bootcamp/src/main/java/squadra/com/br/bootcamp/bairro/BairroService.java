package squadra.com.br.bootcamp.bairro;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BairroService {
    private final BairroRepository bairroRepository;

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

}
