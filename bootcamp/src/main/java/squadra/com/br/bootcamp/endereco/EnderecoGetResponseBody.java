package squadra.com.br.bootcamp.endereco;

import lombok.Data;
import squadra.com.br.bootcamp.bairro.BairroGetResponseBody;

@Data
public class EnderecoGetResponseBody {
    private Long codigoEndereco;
    private Long codigoPessoa;
    private Long codigoBairro;
    private String nomeRua;
    private String numero;
    private String complemento;
    private String cep;
    private BairroGetResponseBody bairro = new BairroGetResponseBody();
}
