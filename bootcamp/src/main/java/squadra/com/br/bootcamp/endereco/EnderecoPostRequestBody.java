package squadra.com.br.bootcamp.endereco;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnderecoPostRequestBody {

    @NotNull(message = "O campo codigoPessoa não pode ser nulo.")
    @Column(name = "CODIGO_PESSOA")
    private Long codigoPessoa;

    @NotNull(message = "O campo codigoBairro não pode ser nulo.")
    @Column(name = "CODIGO_BAIRRO")
    private Long codigoBairro;

    @NotNull(message = "O campo nomeRua não pode ser nulo.")
    @Size(min = 1, max = 256, message = "O nome deve ter entre 1 e 256 caracteres.")
    private String nomeRua;

    @NotNull(message = "O campo numero não pode ser nulo.")
    private String numero;

    @NotNull(message = "O campo complemento não pode ser nulo.")
    private String complemento;

    @NotNull(message = "O campo cep não pode ser nulo.")
    private String cep;
}
