package squadra.com.br.bootcamp.uf;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UfMapper {
    UfGetResponseBody toGetResponseBody(UfVo uf);
}
