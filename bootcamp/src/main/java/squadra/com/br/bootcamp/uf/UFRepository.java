package squadra.com.br.bootcamp.uf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UFRepository extends JpaRepository<UfVo, Long> {

    @Query("SELECT u FROM UfVo u " +
            "WHERE (:codigoUF IS NULL OR u.codigoUF = :codigoUF) " +
            "AND (:sigla IS NULL OR u.sigla = :sigla) " +
            "AND (:nome IS NULL OR u.nome = :nome) " +
            "AND (:status IS NULL OR u.status = :status)" +
            "ORDER BY u.codigoUF DESC")
    List<UfVo> findByParams(Long codigoUF, String sigla, String nome, Integer status);

    List<UfVo> findAllByOrderByCodigoUFDesc();
}
