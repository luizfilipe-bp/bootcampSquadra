package squadra.com.br.bootcamp.pessoa.ResponseGet;

import lombok.Data;

@Data
public class UfGetResponseBody {
    private Long codigoUF;
    private String sigla;
    private String nome;
    private Integer status;
}
