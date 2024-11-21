package squadra.com.br.bootcamp.municipio;

import org.mapstruct.Mapper;
import squadra.com.br.bootcamp.pessoa.ResponseGet.MunicipioGetResponseBody;

@Mapper(componentModel = "spring")
public interface MunicipioMapper {
    MunicipioGetResponseBody toGetResponseBody(MunicipioVo municipio);
}
