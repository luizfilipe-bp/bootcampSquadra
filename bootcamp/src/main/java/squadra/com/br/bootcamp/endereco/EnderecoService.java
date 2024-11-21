package squadra.com.br.bootcamp.endereco;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import squadra.com.br.bootcamp.bairro.BairroService;
import squadra.com.br.bootcamp.exception.RegistroNaoExisteNoBancoException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final BairroService bairroService;
    private final EnderecoMapper enderecoMapper;

    public void save(List<EnderecoPostRequestBody> enderecosPostRequest) throws RegistroNaoExisteNoBancoException {
        List<EnderecoVo> enderecosVo = enderecosPostRequest.stream()
                .map(enderecoMapper::toEnderecoVo)
                .toList();
        enderecoRepository.saveAll(enderecosVo);
    }

    public void verificaEnderecosValidos(List<EnderecoPostRequestBody> enderecoPostRequestBody){
        enderecoPostRequestBody.forEach(endereco -> bairroService.verificaExisteBairroCadastrado(endereco.getCodigoBairro()));
    }
    public List<EnderecoVo> buscarEnderecosPorCodigoPessoa(Long codigoPessoa){
        return enderecoRepository.findAllByCodigoPessoa(codigoPessoa);
    }

    public EnderecoGetResponseBody converterEnderecoVoParaGetResponseBody(EnderecoVo endereco){
        return enderecoMapper.toGetResponseBody(endereco);
    }
}
