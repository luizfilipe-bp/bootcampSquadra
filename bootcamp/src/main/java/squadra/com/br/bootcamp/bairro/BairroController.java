package squadra.com.br.bootcamp.bairro;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bairro")
@RequiredArgsConstructor
public class BairroController {
    private final BairroService bairroService;

    @GetMapping
    public ResponseEntity<Object> findByParams(@RequestParam(required = false) Long codigoBairro,
                                               @RequestParam(required = false) Long codigoMunicipio,
                                               @RequestParam(required = false) String nome,
                                               @RequestParam(required = false) Integer status){

        Object result = bairroService.findByParams(codigoBairro, codigoMunicipio, nome, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<List<BairroVo>> save(@Valid @RequestBody BairroVo bairro){
        return new ResponseEntity<>(bairroService.save(bairro), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<List<BairroVo>> update(@Valid @RequestBody BairroVo bairro){
        return new ResponseEntity<>(bairroService.update(bairro), HttpStatus.OK);
    }
}
