package squadra.com.br.bootcamp.pessoa;

import lombok.Data;
import squadra.com.br.bootcamp.endereco.EnderecoGetRequestBody;

import java.util.Collections;
import java.util.List;

@Data
public class PessoaGetRequestBody {
    private Long codigoPessoa;
    private String nome;
    private String sobrenome;
    private Integer idade;
    private String login;
    private String senha;
    private Integer status;
    private List<EnderecoGetRequestBody> enderecos = Collections.emptyList();
}
