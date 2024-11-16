package squadra.com.br.bootcamp.uf;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UFService {
    private final UFRepository ufRepository;

    public Object findByParams(Long codigoUF, String sigla, String nome, Integer status) {
        try {
            if(sigla != null){
                sigla = sigla.trim();
            }
            if(nome != null){
                nome = nome.trim();
            }
            List<UfVo> result = ufRepository.findByParams(codigoUF, sigla, nome, status);
            if ((codigoUF != null || (sigla != null && !sigla.isEmpty()) || (nome != null && !nome.isEmpty())) && !result.isEmpty()) {
                return result.getFirst();
            }
            return result;

        }catch (RuntimeException e){
            System.out.println("Erro na consulta de UF\n" + e.getMessage());
            throw new ExcecaoPersonalizada("Não foi possível consultar UF no banco de dados.");
        }
    }

    public List<UfVo> save(UfVo uf){
        if(existeUfComMesmoSiglaOuNome(uf)){
            throw new ExcecaoPersonalizada("Já existe uma UF com o mesmo nome ou sigla.");
        }
        ufRepository.save(uf);
        return ufRepository.findAllByOrderByCodigoUFDesc();
    }

    public List<UfVo> update(UfVo uf){
        Optional<UfVo> ufVoAntigo = ufRepository.findById(uf.getCodigoUF());
        if (ufVoAntigo.isEmpty()) {
            throw new ExcecaoPersonalizada("UF com código " + uf.getCodigoUF() + " não encontrada.");
        }

        boolean mesmaUf = ufVoAntigo.get().getCodigoUF().equals(uf.getCodigoUF()) &&
                ufVoAntigo.get().getSigla().equals(uf.getSigla()) &&
                ufVoAntigo.get().getNome().equals(uf.getNome());
        if(mesmaUf || !existeUfComMesmoSiglaOuNome(uf)) {

            try {
                ufVoAntigo.get().setNome(uf.getNome());
                ufVoAntigo.get().setSigla(uf.getSigla());
                ufVoAntigo.get().setStatus(uf.getStatus());
                ufRepository.save(ufVoAntigo.get());

            } catch (RuntimeException e) {
                throw new ExcecaoPersonalizada("Não foi possível realizar a alteração de uf de códigoUF " + uf.getCodigoUF());
            }
        }else{
            throw new ExcecaoPersonalizada("Já existe uma UF com o mesmo nome ou sigla.");
        }
        return ufRepository.findAllByOrderByCodigoUFDesc();
    }

    private boolean existeUfComMesmoSiglaOuNome(UfVo uf){
         boolean ufExisteNoBanco = ufRepository.findAll().stream()
                 .anyMatch(ufNoBanco ->
                         ufNoBanco.getNome().equalsIgnoreCase(uf.getNome()) ||
                         ufNoBanco.getSigla().equalsIgnoreCase(uf.getSigla()));
        return ufExisteNoBanco;
    }
}
