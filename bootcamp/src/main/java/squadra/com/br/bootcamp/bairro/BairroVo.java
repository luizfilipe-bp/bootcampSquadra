package squadra.com.br.bootcamp.bairro;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@Table(name = "TB_BAIRRO")
public class BairroVo {

    @Id
    @GeneratedValue(generator = "sequenceCodigoBairro", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequenceCodigoBairro", sequenceName = "SEQUENCE_BAIRRO", allocationSize = 1)
    @Column(name = "CODIGO_BAIRRO")
    private Long codigoBairro;

    @Column(name = "CODIGO_MUNICIPIO")
    private Long codigoMunicipio;

    private String nome;

    private Integer status;
}
