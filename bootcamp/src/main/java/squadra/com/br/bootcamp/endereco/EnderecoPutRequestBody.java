package squadra.com.br.bootcamp.endereco;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnderecoPutRequestBody {

    private Long codigoEndereco;

    @NotNull(message = "O campo codigoPessoa não pode ser nulo.")
    @Digits(integer = 9, fraction = 0, message = "O codigoPessoa deve ser um número inteiro de 9 digitos")
    private Long codigoPessoa;

    @NotNull(message = "O campo codigoBairro não pode ser nulo.")
    @Digits(integer = 9, fraction = 0, message = "O codigoBairro deve ser um número inteiro de 9 digitos")
    private Long codigoBairro;

    @NotNull(message = "O campo nomeRua não pode ser nulo.")
    @Size(min = 1, max = 256, message = "O nome deve ter entre 1 e 256 caracteres.")
    private String nomeRua;

    @NotNull(message = "O campo numero não pode ser nulo.")
    @Size(max = 10, message = "O numero deve ter no máximo 10 caracteres")
    private String numero;

    @Size(max = 20, message = "O complemento deve ter no máximo 20 caracteres.")
    private String complemento;

    @NotNull(message = "O campo cep não pode ser nulo.")
    @Size(max = 10, message = "O cep deve ter no máximo 10 caracteres.")
    private String cep;
}
