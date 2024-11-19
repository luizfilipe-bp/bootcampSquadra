package squadra.com.br.bootcamp.exception;

public class RegistroNaoExisteNoBanco extends ExcecaoPersonalizada{
    public RegistroNaoExisteNoBanco(String mensagem){
        super(mensagem);
    }
}
