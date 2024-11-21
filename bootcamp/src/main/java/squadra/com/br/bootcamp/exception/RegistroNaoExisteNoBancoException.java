package squadra.com.br.bootcamp.exception;

public class RegistroNaoExisteNoBancoException extends ExcecaoPersonalizadaException {
    public RegistroNaoExisteNoBancoException(String mensagem){
        super(mensagem);
    }
}
