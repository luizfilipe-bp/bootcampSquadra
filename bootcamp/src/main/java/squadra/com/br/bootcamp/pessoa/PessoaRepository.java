package squadra.com.br.bootcamp.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository extends JpaRepository<PessoaVo, Long> {
    Optional<PessoaVo> findByLogin(String login);
    List<PessoaVo> findAllByStatusOrderByCodigoPessoaDesc(Integer status);
}
