package squadra.com.br.bootcamp.endereco;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    EnderecoGetResponseBody toGetResponseBody(EnderecoVo endereco);
    EnderecoVo toEnderecoVo(EnderecoPostRequestBody endereco);
}
