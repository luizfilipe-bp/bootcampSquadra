package squadra.com.br.bootcamp.pessoa;

import jakarta.validation.constraints.*;
import lombok.Data;
import squadra.com.br.bootcamp.endereco.EnderecoPostRequestBody;

import java.util.List;

@Data
public class PessoaPostRequestBody {
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

    @NotNull(message = "o campo endereços deve possuir pelo menos um endereço")
    @NotEmpty(message = "o campo endereços deve possuir pelo menos um endereço")
    private List<EnderecoPostRequestBody> enderecos;
}
