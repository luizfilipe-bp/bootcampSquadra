package squadra.com.br.bootcamp.municipio;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MunicipioMapper {
    MunicipioGetResponseBody toGetResponseBody(MunicipioVo municipio);
}
