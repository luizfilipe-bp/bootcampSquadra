package squadra.com.br.bootcamp.endereco;

import lombok.Data;
import squadra.com.br.bootcamp.bairro.BairroGetRequestBody;

@Data
public class EnderecoGetRequestBody {
    private Long codigoEndereco;
    private Long codigoPessoa;
    private Long codigoBairro;
    private String nomeRua;
    private String numero;
    private String complemento;
    private String cep;
    private BairroGetRequestBody bairro = new BairroGetRequestBody();
}
