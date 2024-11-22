package squadra.com.br.bootcamp.endereco;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import squadra.com.br.bootcamp.bairro.BairroService;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizadaException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final BairroService bairroService;
    private final EnderecoMapper enderecoMapper;

    public void save(List<EnderecoVo> enderecosVo) throws ExcecaoPersonalizadaException{
        enderecoRepository.saveAll(enderecosVo);
    }

    public void delete(List<EnderecoVo> enderecosVo){
        enderecoRepository.deleteAll(enderecosVo);
    }

    public void verificaEnderecosValidos(List<EnderecoPostRequestBody> enderecoPostRequestBody){
        enderecoPostRequestBody.forEach(endereco -> bairroService.verificaExisteBairroCadastrado(endereco.getCodigoBairro()));
    }
    public List<EnderecoVo> buscarEnderecosPorCodigoPessoa(Long codigoPessoa){
        return enderecoRepository.findAllByCodigoPessoa(codigoPessoa);
    }

    public EnderecoGetResponseBody converterEnderecoVoParaGetResponseBody(EnderecoVo enderecoVo){
        return enderecoMapper.toGetResponseBody(enderecoVo);
    }

    public EnderecoVo converterParaEnderecoVo(EnderecoPostRequestBody enderecoPostRequestBody){
        return enderecoMapper.toEnderecoVo(enderecoPostRequestBody);
    }

    public EnderecoVo converterParaEnderecoVo(EnderecoPutRequestBody enderecoPutRequestBody){
        return enderecoMapper.toEnderecoVo(enderecoPutRequestBody);
    }
}
