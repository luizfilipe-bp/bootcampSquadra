package squadra.com.br.bootcamp.bairro;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
