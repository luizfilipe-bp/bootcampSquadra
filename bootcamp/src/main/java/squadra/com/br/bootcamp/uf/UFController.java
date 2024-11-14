package squadra.com.br.bootcamp.uf;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uf")
@RequiredArgsConstructor
public class UFController {
    private final UFService ufService;

    @GetMapping
    public ResponseEntity<Object> findByParams(
            @RequestParam(required = false) Long codigoUF,
            @RequestParam(required = false) String sigla,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status) {

        Object result = ufService.findByParams(codigoUF, sigla, nome, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<UfVo> save(@RequestBody @Valid UfVo uf){
        return new ResponseEntity<>(ufService.save(uf), HttpStatus.OK);
    }

}
