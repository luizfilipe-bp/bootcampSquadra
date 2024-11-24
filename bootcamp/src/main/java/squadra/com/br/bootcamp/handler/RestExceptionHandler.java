package squadra.com.br.bootcamp.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizadaException;
import squadra.com.br.bootcamp.exception.ApiErrorFormat;

import java.util.Optional;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ExcecaoPersonalizadaException.class,
                        RuntimeException.class})
    public ResponseEntity<ApiErrorFormat> handleGenericException(RuntimeException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ex.printStackTrace();
        ApiErrorFormat apiErrorFormat = ApiErrorFormat.builder()
                .mensagem(ex.getMessage())
                .status(status.value())
                .build();
        return new ResponseEntity<>(apiErrorFormat, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorFormat> handleArgumentNotValid(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String mensagemErro = Optional.ofNullable(ex.getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Erro de validação desconhecido.");

        ApiErrorFormat apiErrorFormat = ApiErrorFormat.builder()
                .mensagem(mensagemErro)
                .status(status.value())
                .build();

        return new ResponseEntity<>(apiErrorFormat, status);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorFormat> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String mensagem = String.format("O campo '%s' recebeu o valor '%s' que não é do tipo correto para este campo. O tipo correto é '%s'.",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido");

        ApiErrorFormat apiErrorFormat = ApiErrorFormat.builder()
                .mensagem(mensagem)
                .status(status.value())
                .build();

        return new ResponseEntity<>(apiErrorFormat, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorFormat> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String mensagem = "Erro ao processar a requisição.";
        String campoErro = "Campo desconhecido";

        if (ex.getCause() instanceof JsonMappingException jsonEx) {
            if (!jsonEx.getPath().isEmpty()) {
                campoErro = jsonEx.getPath().getFirst().getFieldName();
                mensagem = "Erro no campo '" + campoErro + "': valor inválido ou formato incorreto.";
            }
        }
        ApiErrorFormat errorResponse = ApiErrorFormat.builder()
                .mensagem(mensagem)
                .status(status.value())
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorFormat> handleDataIntegrityViolation(RuntimeException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ex.printStackTrace();
        ApiErrorFormat apiErrorFormat = ApiErrorFormat.builder()
                .mensagem("Houve um erro de integridade no banco de dados.")
                .status(status.value())
                .build();
        return new ResponseEntity<>(apiErrorFormat, status);
    }


}
