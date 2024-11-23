package squadra.com.br.bootcamp.municipio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/municipio")
@RequiredArgsConstructor
public class MunicipioController {
    private final MunicipioService municipioService;

    @GetMapping
    public ResponseEntity<Object> findByParams(
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) Long codigoUF,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status) {

        Object result = municipioService.findByParams(codigoMunicipio, codigoUF, nome, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<List<MunicipioVo>> save(@Valid @RequestBody MunicipioPostRequestBody municipioPostRequestBody) {
        return new ResponseEntity<>(municipioService.save(municipioPostRequestBody), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<List<MunicipioVo>> update(@Valid @RequestBody MunicipioPutRequestBody municipioPutRequestBody) {
        return new ResponseEntity<>(municipioService.update(municipioPutRequestBody), HttpStatus.OK);
    }
}
