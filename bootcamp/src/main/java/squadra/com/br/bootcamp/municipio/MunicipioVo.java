package squadra.com.br.bootcamp.municipio;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "TB_MUNICIPIO")
public class MunicipioVo {

    @Id
    @GeneratedValue(generator = "sequenceCodigoMunicipio", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequenceCodigoMunicipio", sequenceName = "SEQUENCE_MUNICIPIO", allocationSize = 1)
    @Column(name = "CODIGO_MUNICIPIO")
    private Long codigoMunicipio;

    @NotNull(message = "O campo codigoUF não pode ser nulo.")
    @Column(name = "CODIGO_UF")
    private Long codigoUF;

    @NotNull(message = "O campo nome não pode ser nulo.")
    @Size(min = 1, max = 256, message = "O nome deve ter entre 1 e 256 caracteres.")
    private String nome;

    @NotNull(message = "O campo status não pode ser nulo.")
    @Min(value = 1, message = "o campo status deve ser 1 ou 2")
    @Max(value = 2, message = "o campo status deve ser 1 ou 2")
    private Integer status;

}
