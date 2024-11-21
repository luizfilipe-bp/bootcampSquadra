package squadra.com.br.bootcamp.exception;

public class RegistroJaExisteNoBancoException extends ExcecaoPersonalizadaException {
    public RegistroJaExisteNoBancoException(String mensagem){
        super(mensagem);
    }
}
