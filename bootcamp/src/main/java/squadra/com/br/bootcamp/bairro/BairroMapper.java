package squadra.com.br.bootcamp.bairro;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BairroMapper {
    BairroGetResponseBody toGetResponseBody(BairroVo bairroVo);
}
