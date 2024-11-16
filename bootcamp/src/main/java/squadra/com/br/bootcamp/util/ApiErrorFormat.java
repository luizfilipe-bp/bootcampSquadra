package squadra.com.br.bootcamp.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorFormat {
    private String mensagem;
    private Integer codigo;
}
