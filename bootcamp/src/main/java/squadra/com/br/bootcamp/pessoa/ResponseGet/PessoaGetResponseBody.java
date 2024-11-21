package squadra.com.br.bootcamp.pessoa.ResponseGet;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PessoaGetResponseBody {
    private Long codigoPessoa;
    private String nome;
    private String sobrenome;
    private Integer idade;
    private String login;
    private String senha;
    private Integer status;
    private List<EnderecoGetResponseBody> enderecos = Collections.emptyList();
}
