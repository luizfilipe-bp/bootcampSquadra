package squadra.com.br.bootcamp.pessoa;

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
@Table(name = "TB_PESSOA")
public class PessoaVo {
    @Id
    @GeneratedValue(generator = "sequenceCodigoPessoa", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequenceCodigoPessoa", sequenceName = "SEQUENCE_PESSOA", allocationSize = 1)
    @Column(name = "CODIGO_PESSOA")
    private Long codigoPessoa;

    @NotNull(message = "O campo nome não pode ser nulo.")
    @Size(min = 1, max = 256, message = "O nome deve ter entre 1 e 256 caracteres.")
    private String nome;

    @NotNull(message = "O campo sobrenome não pode ser nulo.")
    @Size(min = 1, max = 256, message = "O nome deve ter entre 1 e 256 caracteres.")
    private String sobrenome;

    @NotNull(message = "O campo idade não pode ser nulo.")
    @Digits(integer = 3, fraction = 0, message = "O campo idade deve conter no máximo 3 dígitos.")
    private Integer idade;

    @NotNull(message = "O campo login não pode ser nulo.")
    @Size(min = 1, max = 50, message = "O login deve ter entre 1 e 256 caracteres.")
    private String login;

    @NotNull(message = "O campo senha não pode ser nulo.")
    @Size(min = 1, max = 50, message = "O senha deve ter entre 1 e 256 caracteres.")
    private String senha;

    @NotNull(message = "O campo status não pode ser nulo.")
    @Min(value = 1, message = "o campo status deve ser 1 ou 2")
    @Max(value = 2, message = "o campo status deve ser 1 ou 2")
    private Integer status;
}
