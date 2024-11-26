package squadra.com.br.bootcamp.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaVo, Long> {
    Optional<PessoaVo> findByLogin(String login);
    List<PessoaVo> findAllByStatusOrderByCodigoPessoaDesc(Integer status);
}
