package squadra.com.br.bootcamp.bairro;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BairroMapper {
    BairroVo toBairroVo(BairroPostRequestBody bairroPutRequestBody);
    BairroVo toBairroVo(BairroPutRequestBody bairroPutRequestBody);
    BairroGetRequestBody toGetResponseBody(BairroVo bairroVo);
}
