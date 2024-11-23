package squadra.com.br.bootcamp.municipio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@Table(name = "TB_MUNICIPIO")
public class MunicipioVo {

    @Id
    @GeneratedValue(generator = "sequenceCodigoMunicipio", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequenceCodigoMunicipio", sequenceName = "SEQUENCE_MUNICIPIO", allocationSize = 1)
    @Column(name = "CODIGO_MUNICIPIO")
    private Long codigoMunicipio;

    @Column(name = "CODIGO_UF")
    private Long codigoUF;

    private String nome;

    private Integer status;
}
