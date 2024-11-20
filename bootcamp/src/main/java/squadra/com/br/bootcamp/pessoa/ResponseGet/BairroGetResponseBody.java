package squadra.com.br.bootcamp.pessoa.ResponseGet;

import lombok.Data;

@Data
public class BairroGetResponseBody {
    private Long codigoBairro;
    private Long codigoMunicipio;
    private String nome;
    private Integer status;
    private MunicipioGetResponseBody municipio = new MunicipioGetResponseBody();
}
