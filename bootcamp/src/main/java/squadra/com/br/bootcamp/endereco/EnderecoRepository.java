package squadra.com.br.bootcamp.endereco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EnderecoRepository extends JpaRepository<EnderecoVo, Long>{
    List<EnderecoVo> findAllByCodigoPessoa(Long codigoPessoa);
}
