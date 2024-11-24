package squadra.com.br.bootcamp.bairro;

import lombok.Data;
import squadra.com.br.bootcamp.municipio.MunicipioGetResponseBody;

@Data
public class BairroGetRequestBody {
    private Long codigoBairro;
    private Long codigoMunicipio;
    private String nome;
    private Integer status;
    private MunicipioGetResponseBody municipio = new MunicipioGetResponseBody();
}
