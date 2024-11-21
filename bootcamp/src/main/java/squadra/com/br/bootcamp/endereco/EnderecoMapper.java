package squadra.com.br.bootcamp.endereco;


import org.mapstruct.Mapper;
import squadra.com.br.bootcamp.pessoa.ResponseGet.EnderecoGetResponseBody;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    EnderecoGetResponseBody toGetResponseBody(EnderecoVo endereco);
}
