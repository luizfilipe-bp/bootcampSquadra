package squadra.com.br.bootcamp.uf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UFRepository extends JpaRepository<UfVo, Long> {
    List<UfVo> findAllByOrderByCodigoUFDesc();
}
