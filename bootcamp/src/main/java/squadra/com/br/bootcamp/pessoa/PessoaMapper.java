package squadra.com.br.bootcamp.pessoa;

import org.mapstruct.Mapper;
import squadra.com.br.bootcamp.pessoa.ResponseGet.PessoaGetResponseBody;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PessoaMapper {
    PessoaGetResponseBody toGetResponseBody(PessoaVo pessoa);
}

