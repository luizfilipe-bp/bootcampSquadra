package squadra.com.br.bootcamp.uf;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UFService {
    private final UFRepository ufRepository;

    public Object findByParams(Long codigoUF, String sigla, String nome, Integer status){
        List<UfVo> result = ufRepository.findByParams(codigoUF, sigla, nome, status);
        if((codigoUF != null || sigla != null || nome != null) && !result.isEmpty()) {
            return result.getFirst();
        }
        return result;
    }

    public UfVo save(UfVo uf){
        return ufRepository.save(uf);
    }
}
