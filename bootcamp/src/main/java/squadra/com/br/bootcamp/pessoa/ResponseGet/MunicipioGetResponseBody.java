package squadra.com.br.bootcamp.pessoa.ResponseGet;

import lombok.Data;

@Data
public class MunicipioGetResponseBody {
    private Long codigoMunicipio;
    private Long codigoUF;
    private String nome;
    private Integer status;
    private UfGetResponseBody uf = new UfGetResponseBody();
}
