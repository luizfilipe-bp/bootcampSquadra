package squadra.com.br.bootcamp.municipio;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MunicipioPostRequestBody {
    @NotNull(message = "O campo codigoUF não pode ser nulo.")
    private Long codigoUF;

    @NotNull(message = "O campo nome não pode ser nulo.")
    @Size(min = 1, max = 256, message = "O nome deve ter entre 1 e 256 caracteres.")
    private String nome;

    @NotNull(message = "O campo status não pode ser nulo.")
    @Min(value = 1, message = "o campo status deve ser 1 ou 2")
    @Max(value = 2, message = "o campo status deve ser 1 ou 2")
    private Integer status;
}
