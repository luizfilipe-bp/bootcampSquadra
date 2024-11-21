package squadra.com.br.bootcamp.uf;

import org.mapstruct.Mapper;
import squadra.com.br.bootcamp.pessoa.ResponseGet.UfGetResponseBody;


@Mapper(componentModel = "spring")
public interface UfMapper {
    UfGetResponseBody toGetResponseBody(UfVo uf);
}
