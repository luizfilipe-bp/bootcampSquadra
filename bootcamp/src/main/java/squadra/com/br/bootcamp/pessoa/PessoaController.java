package squadra.com.br.bootcamp.pessoa;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import squadra.com.br.bootcamp.pessoa.ResponseGet.PessoaGetResponseBody;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<List<PessoaGetResponseBody>> save(@Valid @RequestBody PessoaPostRequestBody pessoaPostRequestBody){
        return new ResponseEntity<>(pessoaService.save(pessoaPostRequestBody), HttpStatus.OK);
    }
}
