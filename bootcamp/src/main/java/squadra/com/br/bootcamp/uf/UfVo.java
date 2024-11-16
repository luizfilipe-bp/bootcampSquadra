package squadra.com.br.bootcamp.uf;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "tb_uf")
public class UfVo {

    @Id
    @NotNull(message = "0 campo codigoUF não pode ser nulo.")
    @GeneratedValue(generator = "sequenceCodigoUf", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequenceCodigoUf", sequenceName = "SEQUENCE_UF", allocationSize = 1)
    @Column(name = "codigo_uf")
    private Long codigoUF;

    @NotNull(message = "0 campo sigla não pode ser nulo.")
    @Column(length = 2)
    @Pattern(regexp = "^[A-Za-z]{2}$", message = "A sigla deve ser composta apenas de duas letras.")
    private String sigla;

    @NotNull(message = "O campo nome não pode ser nulo.")
    @Size(min = 1, max = 60, message = "O campo nome deve ter de 1 até 60 caracteres.")
    private String nome;

    @NotNull(message = "0 campo status não pode ser nulo.")
    @Min(value = 1, message = "o campo status deve ser 1 ou 2")
    @Max(value = 2, message = "o campo status deve ser 1 ou 2")
    private Integer status;

}
