package squadra.com.br.bootcamp.bairro;

import org.mapstruct.Mapper;
import squadra.com.br.bootcamp.pessoa.ResponseGet.BairroGetResponseBody;

@Mapper(componentModel = "spring")
public interface BairroMapper {
    BairroGetResponseBody toGetResponseBody(BairroVo bairroVo);
}
