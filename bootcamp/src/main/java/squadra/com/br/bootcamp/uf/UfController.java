package squadra.com.br.bootcamp.uf;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uf")
@RequiredArgsConstructor
public class UfController {
    private final UfService ufService;

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
    public ResponseEntity<List<UfVo>> save(@Valid @RequestBody UfPostRequestBody ufPostRequestBody) {
        return new ResponseEntity<>(ufService.save(ufPostRequestBody), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<List<UfVo>> update(@Valid @RequestBody UfPutRequestBody ufPutRequestBody) {
        return new ResponseEntity<>(ufService.update(ufPutRequestBody), HttpStatus.OK);
    }

}
