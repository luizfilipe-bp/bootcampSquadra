package squadra.com.br.bootcamp.uf;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@Table(name = "TB_UF")
public class UfVo {

    @Id
    @GeneratedValue(generator = "sequenceCodigoUf", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequenceCodigoUf", sequenceName = "SEQUENCE_UF", allocationSize = 1)
    @Column(name = "CODIGO_UF")
    private Long codigoUF;

    @Column(length = 2)
    private String sigla;

    private String nome;

    private Integer status;
}
