package squadra.com.br.bootcamp.exception;

public class ExcecaoPersonalizadaException extends RuntimeException{
    public ExcecaoPersonalizadaException(String mensagem){
        super(mensagem);
    }
}
