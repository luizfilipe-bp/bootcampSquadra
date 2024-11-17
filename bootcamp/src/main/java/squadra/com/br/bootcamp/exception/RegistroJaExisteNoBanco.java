package squadra.com.br.bootcamp.exception;

public class RegistroJaExisteNoBanco extends ExcecaoPersonalizada{
    public RegistroJaExisteNoBanco(String mensagem){
        super(mensagem);
    }
}
