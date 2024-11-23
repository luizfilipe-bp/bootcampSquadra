package squadra.com.br.bootcamp.municipio;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MunicipioMapper {
    MunicipioVo toMunicipioVo(MunicipioPostRequestBody municipioPostRequestBody);
    MunicipioVo toMunicipioVo(MunicipioPutRequestBody municipioPutRequestBody);
    MunicipioGetResponseBody toGetResponseBody(MunicipioVo municipio);
}
