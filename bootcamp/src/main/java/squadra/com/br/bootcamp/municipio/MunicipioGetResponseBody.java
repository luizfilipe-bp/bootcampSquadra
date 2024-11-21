package squadra.com.br.bootcamp.municipio;

import lombok.Data;
import squadra.com.br.bootcamp.uf.UfGetResponseBody;

@Data
public class MunicipioGetResponseBody {
    private Long codigoMunicipio;
    private Long codigoUF;
    private String nome;
    private Integer status;
    private UfGetResponseBody uf = new UfGetResponseBody();
}
