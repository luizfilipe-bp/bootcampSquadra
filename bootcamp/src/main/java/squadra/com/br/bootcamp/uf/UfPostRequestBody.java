package squadra.com.br.bootcamp.uf;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UfPostRequestBody {
    @NotNull(message = "0 campo sigla não pode ser nulo.")
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
