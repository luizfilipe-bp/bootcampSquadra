package squadra.com.br.bootcamp.uf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfRepository extends JpaRepository<UfVo, Long> {

}
