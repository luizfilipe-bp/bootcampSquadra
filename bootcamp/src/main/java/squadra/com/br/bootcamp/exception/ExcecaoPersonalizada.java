package squadra.com.br.bootcamp.exception;

public class ExcecaoPersonalizada extends RuntimeException{
    public ExcecaoPersonalizada(String mensagem){
        super(mensagem);
    }
}
