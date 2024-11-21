package squadra.com.br.bootcamp.pessoa;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {
    private final PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<Object> findByParams(
            @RequestParam(required = false) Long codigoPessoa,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) Integer status){

        Object result = pessoaService.findByParams(codigoPessoa, login, status);
        return ResponseEntity.ok(result);
    }
}
