package squadra.com.br.bootcamp.uf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizadaException;
import squadra.com.br.bootcamp.exception.RegistroJaExisteNoBancoException;
import squadra.com.br.bootcamp.exception.RegistroNaoExisteNoBancoException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UfService {
    private final UfRepository ufRepository;
    private final UfMapper ufMapper;

    public Object findByParams(Long codigoUF, String sigla, String nome, Integer status) {
        try {
            if(sigla != null){
                sigla = sigla.trim();
            }
            if(nome != null){
                nome = nome.trim();
            }

            List<UfVo> ufsFiltradas = filtrarUfsEOrdenarPorCodigoUF(codigoUF, sigla, nome, status);
            if ((codigoUF != null || (sigla != null && !sigla.isEmpty()) || (nome != null && !nome.isEmpty())) && !ufsFiltradas.isEmpty()) {
                return ufsFiltradas.getFirst();
            }
            return ufsFiltradas;

        }catch (RuntimeException e){
            System.out.println("Erro na consulta de UF" + e.getMessage());
            throw new ExcecaoPersonalizadaException("Não foi possível consultar UF no banco de dados.");
        }
    }

    @Transactional
    public List<UfVo> save(UfVo uf){
        try{
            verificaExisteUfComMesmoSiglaOuNome(uf);
            ufRepository.save(uf);
            return filtrarUfsEOrdenarPorCodigoUF(null, null, null, null);

        }catch (ExcecaoPersonalizadaException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar a UF " + uf.getNome() + ". " + ex.getMessage());

        }catch (RuntimeException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar a UF" + uf.getNome());
        }
    }

    @Transactional
    public List<UfVo> update(UfVo uf){
        try {
            verificaCodigoUFNulo(uf.getCodigoUF());
            verificaExisteUf(uf.getCodigoUF());
            verificaExisteUfComMesmoSiglaOuNome(uf);

            Optional<UfVo> ufVoAntigo = ufRepository.findById(uf.getCodigoUF());
            if(ufVoAntigo.isPresent()) {
                ufVoAntigo.get().setNome(uf.getNome());
                ufVoAntigo.get().setSigla(uf.getSigla());
                ufVoAntigo.get().setStatus(uf.getStatus());
                ufRepository.save(ufVoAntigo.get());
            }

        }catch (ExcecaoPersonalizadaException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível realizar a alteração de uf de códigoUF " + uf.getCodigoUF() + ". " + ex.getMessage());

        }catch (RuntimeException ex) {
            throw new ExcecaoPersonalizadaException("Não foi possível realizar a alteração de uf de códigoUF " + uf.getCodigoUF());
        }
        return filtrarUfsEOrdenarPorCodigoUF(null, null, null, null);
    }

    private void verificaExisteUfComMesmoSiglaOuNome(UfVo uf) throws RegistroJaExisteNoBancoException {
        List<UfVo> todosUfs = ufRepository.findAll();
        boolean ufsComNomesIguais = todosUfs.stream()
                .anyMatch(ufNoBanco -> ufNoBanco.getNome().equalsIgnoreCase(uf.getNome()) &&
                                       !ufNoBanco.getCodigoUF().equals(uf.getCodigoUF()));
        if(ufsComNomesIguais){
            throw new RegistroJaExisteNoBancoException("Ja existe uma UF cadastrada com o mesmo nome. ");
        }

        boolean ufsComSiglasIguais = todosUfs.stream()
                .anyMatch(ufNoBanco -> ufNoBanco.getSigla().equalsIgnoreCase(uf.getSigla()) &&
                !ufNoBanco.getCodigoUF().equals(uf.getCodigoUF()));
        if (ufsComSiglasIguais) {
            throw new RegistroJaExisteNoBancoException("Já existe uma UF cadastrada com a mesma sigla.");
        }
    }

    private List<UfVo> filtrarUfsEOrdenarPorCodigoUF(Long codigoUF, String sigla, String nome, Integer status){
        List<UfVo> todasUfs = ufRepository.findAll();
        return todasUfs.stream()
                .filter(uf -> codigoUF == null || uf.getCodigoUF().equals(codigoUF))
                .filter(uf -> sigla == null || uf.getSigla().equals(sigla))
                .filter(uf -> nome == null || nome.isEmpty() || uf.getNome().equals(nome))
                .filter(uf -> status == null || uf.getStatus().equals(status))
                .sorted(Comparator.comparing(UfVo::getCodigoUF).reversed())
                .collect(Collectors.toList());
    }

    private void verificaCodigoUFNulo(Long codigoUF){
        if(codigoUF == null){
            throw new ExcecaoPersonalizadaException("O campo codigoUF não pode ser nulo.");
        }
    }

    public void verificaExisteUf(Long codigoUF) throws RegistroNaoExisteNoBancoException {
        if(!ufRepository.existsById(codigoUF)){
            throw new RegistroNaoExisteNoBancoException("A UF de código " + codigoUF + " não existe no banco de dados.");
        }
    }

    public UfVo buscarUfPorCodigoUf(Long codigoUF) throws RegistroNaoExisteNoBancoException {
        Optional<UfVo> uf = ufRepository.findById(codigoUF);
        if(uf.isPresent()){
            return uf.get();
        }
        throw new RegistroNaoExisteNoBancoException("Não existe UF de codigoUF " + codigoUF);
    }

    public UfGetResponseBody converterUfVoParaGetResponseBody(UfVo ufVo){
        return ufMapper.toGetResponseBody(ufVo);
    }
}
