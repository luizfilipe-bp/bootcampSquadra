package squadra.com.br.bootcamp.exception;

public class registroJaExisteNoBanco extends ExcecaoPersonalizada{
    public registroJaExisteNoBanco(String mensagem){
        super(mensagem);
    }
}
