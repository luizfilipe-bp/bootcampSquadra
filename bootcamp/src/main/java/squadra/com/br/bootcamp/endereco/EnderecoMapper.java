package squadra.com.br.bootcamp.endereco;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    EnderecoGetRequestBody toGetResponseBody(EnderecoVo enderecoVo);
    EnderecoVo toEnderecoVo(EnderecoPostRequestBody enderecoPostRequestBody);
    EnderecoVo toEnderecoVo(EnderecoPutRequestBody enderecoPutRequestBody);
}
