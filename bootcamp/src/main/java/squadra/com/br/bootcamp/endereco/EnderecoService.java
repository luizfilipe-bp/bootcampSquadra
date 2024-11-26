package squadra.com.br.bootcamp.endereco;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizadaException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;

    @Transactional
    public void save(@Valid List<EnderecoVo> enderecosVo) throws ExcecaoPersonalizadaException{

        enderecoRepository.saveAll(enderecosVo);
    }

    public void delete(List<EnderecoVo> enderecosVo){
        enderecoRepository.deleteAll(enderecosVo);
    }

    public List<EnderecoVo> buscarEnderecosPorCodigoPessoa(Long codigoPessoa){
        return enderecoRepository.findAllByCodigoPessoa(codigoPessoa);
    }

    public EnderecoGetRequestBody converterEnderecoVoParaGetResponseBody(EnderecoVo enderecoVo){
        return enderecoMapper.toGetResponseBody(enderecoVo);
    }

    public EnderecoVo converterParaEnderecoVo(EnderecoPostRequestBody enderecoPostRequestBody){
        return enderecoMapper.toEnderecoVo(enderecoPostRequestBody);
    }

    public EnderecoVo converterParaEnderecoVo(EnderecoPutRequestBody enderecoPutRequestBody){
        return enderecoMapper.toEnderecoVo(enderecoPutRequestBody);
    }
}
