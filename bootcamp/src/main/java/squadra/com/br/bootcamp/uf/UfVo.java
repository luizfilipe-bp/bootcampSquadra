package squadra.com.br.bootcamp.uf;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_uf")
public class UfVo {

    @Id
    @GeneratedValue(generator = "sequenceCodigoUf", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequenceCodigoUf", sequenceName = "SEQUENCE_UF", allocationSize = 1)
    @Column(name = "codigo_uf")
    private Long codigoUF;

    @NotNull
    private String sigla;

    @NotNull
    private String nome;

    @NotNull
    private Integer status;
}
