package squadra.com.br.bootcamp.pessoa;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PessoaMapper {
    PessoaGetResponseBody toGetResponseBody(PessoaVo pessoa);
    PessoaVo toPessoaVo(PessoaPostRequestBody pessoaPostRequestBody);
    PessoaVo toPessoaVo(PessoaPutRequestBody pessoaPutRequestBody);
}

