package squadra.com.br.bootcamp.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;
import squadra.com.br.bootcamp.util.ApiErrorFormat;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ExcecaoPersonalizada.class)
    public ResponseEntity<ApiErrorFormat> genericException(RuntimeException ex){
        ex.printStackTrace();
        ApiErrorFormat apiErrorFormat = ApiErrorFormat.builder()
                .mensagem(ex.getMessage())
                .codigo(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(apiErrorFormat, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorFormat> argumentNotValid(MethodArgumentNotValidException ex){
        ApiErrorFormat apiErrorFormat = ApiErrorFormat.builder()
                .mensagem(ex.getFieldError().getDefaultMessage())
                .codigo(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(apiErrorFormat, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorFormat> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {

        String mensagem = String.format("O campo '%s' recebeu o valor '%s' que não é do tipo correto para este campo. O tipo correto é '%s'.",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido");

        ApiErrorFormat apiErrorFormat = ApiErrorFormat.builder()
                .mensagem(mensagem)
                .codigo(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(apiErrorFormat, HttpStatus.BAD_REQUEST);
    }
}
