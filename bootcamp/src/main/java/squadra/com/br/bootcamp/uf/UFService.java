package squadra.com.br.bootcamp.uf;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;
import squadra.com.br.bootcamp.exception.RegistroJaExisteNoBanco;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

            List<UfVo> todasUfs = ufRepository.findAll();
            List<UfVo> ufsFiltradas = filtrarUfsEOrdenarPorCodigoUF(todasUfs, codigoUF, sigla, nome, status);
            if ((codigoUF != null || (sigla != null && !sigla.isEmpty()) || (nome != null && !nome.isEmpty())) && !ufsFiltradas.isEmpty()) {
                return ufsFiltradas.getFirst();
            }

            return ufsFiltradas;

        }catch (RuntimeException e){
            System.out.println("Erro na consulta de UF\n" + e.getMessage());
            throw new ExcecaoPersonalizada("Não foi possível consultar UF no banco de dados.");
        }
    }

    @Transactional
    public List<UfVo> save(UfVo uf){
        try{
            if(existeUfComMesmoSiglaOuNome(uf)){
                throw new RegistroJaExisteNoBanco("Já existe uma UF com o mesmo nome ou sigla.");
            }
            ufRepository.save(uf);
            return ufRepository.findAllByOrderByCodigoUFDesc();
        }catch (RuntimeException ex){
            throw new ExcecaoPersonalizada("Não foi possível cadastrar a UF");
        }
    }

    @Transactional
    public List<UfVo> update(UfVo uf){
        if(uf.getCodigoUF() == null){
            throw new ExcecaoPersonalizada("O campo codigoUF não pode ser nulo.");
        }
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
            throw new RegistroJaExisteNoBanco("Já existe uma UF com o mesmo nome ou sigla.");
        }
        return ufRepository.findAllByOrderByCodigoUFDesc();
    }

    private boolean existeUfComMesmoSiglaOuNome(UfVo uf){
         return ufRepository.findAll().stream()
                 .anyMatch(ufNoBanco ->
                         ufNoBanco.getNome().equalsIgnoreCase(uf.getNome()) ||
                         ufNoBanco.getSigla().equalsIgnoreCase(uf.getSigla()));
    }

    private List<UfVo> filtrarUfsEOrdenarPorCodigoUF(List<UfVo> listaUfs, Long codigoUF, String sigla, String nome, Integer status){
        return listaUfs.stream()
                .filter(uf -> codigoUF == null || uf.getCodigoUF().equals(codigoUF))
                .filter(uf -> sigla == null || uf.getSigla().equals(sigla))
                .filter(uf -> nome == null || uf.getNome().equals(nome))
                .filter(uf -> status == null || uf.getStatus().equals(status))
                .sorted(Comparator.comparing(UfVo::getCodigoUF).reversed())
                .collect(Collectors.toList());
    }

    public boolean ufExiste(Long codigoUF){
        return ufRepository.existsById(codigoUF);
    }
}
