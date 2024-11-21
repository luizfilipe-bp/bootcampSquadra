package squadra.com.br.bootcamp.uf;

import lombok.Data;

@Data
public class UfGetResponseBody {
    private Long codigoUF;
    private String sigla;
    private String nome;
    private Integer status;
}
