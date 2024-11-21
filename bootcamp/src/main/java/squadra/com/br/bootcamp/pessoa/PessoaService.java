package squadra.com.br.bootcamp.pessoa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import squadra.com.br.bootcamp.bairro.BairroService;
import squadra.com.br.bootcamp.bairro.BairroVo;
import squadra.com.br.bootcamp.endereco.EnderecoService;
import squadra.com.br.bootcamp.endereco.EnderecoVo;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizada;
import squadra.com.br.bootcamp.municipio.MunicipioService;
import squadra.com.br.bootcamp.municipio.MunicipioVo;
import squadra.com.br.bootcamp.pessoa.ResponseGet.*;
import squadra.com.br.bootcamp.uf.UFService;
import squadra.com.br.bootcamp.uf.UfVo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {
    private final EnderecoService enderecoService;
    private final BairroService bairroService;
    private final MunicipioService municipioService;
    private final UFService ufService;

    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;

    public Object findByParams(Long codigoPessoa, String login, Integer status){
        try {
            if (login != null) {
                login = login.trim();
            }
            if (codigoPessoa != null && (login == null || login.isEmpty()) && status == null) {
                Optional<PessoaVo> pessoa = pessoaRepository.findById(codigoPessoa);
                if (pessoa.isPresent()) {
                    List<EnderecoVo> enderecosDaPessoa = enderecoService.buscarEnderecosPorCodigoPessoa(codigoPessoa);

                    List<EnderecoGetResponseBody> enderecosGetResponseBody = enderecosDaPessoa.stream().map(enderecoVo -> {
                        BairroVo bairro = bairroService.buscarBairroPorCodigoBairro(enderecoVo.getCodigoBairro());
                        MunicipioVo municipio = municipioService.buscarMunicipioPorCodigoMunicipio(bairro.getCodigoMunicipio());
                        UfVo uf = ufService.buscarUfPorCodigoUf(municipio.getCodigoUF());

                        UfGetResponseBody ufResponse = ufService.converterUfVoParaGetResponseBody(uf);
                        MunicipioGetResponseBody municipioResponse = municipioService.converterMunicipioParaGetResponseBody(municipio);
                        BairroGetResponseBody bairroResponse = bairroService.converterBairroVoParaGetResponseBody(bairro);
                        EnderecoGetResponseBody enderecoResponse = enderecoService.converterEnderecoVoParaGetResponseBody(enderecoVo);

                        municipioResponse.setUf(ufResponse);
                        bairroResponse.setMunicipio(municipioResponse);
                        enderecoResponse.setBairro(bairroResponse);

                        return enderecoResponse;
                    }).toList();

                    PessoaGetResponseBody pessoaResponse = pessoaMapper.toGetResponseBody(pessoa.get());
                    pessoaResponse.setEnderecos(enderecosGetResponseBody);

                    return pessoaResponse;
                }
            }

            if (codigoPessoa == null && login != null && status == null) {
                Optional<PessoaVo> pessoa = pessoaRepository.findByLogin(login);
                PessoaGetResponseBody pessoaGetResponseBody;
                if (pessoa.isPresent()) {
                    pessoaGetResponseBody = pessoaMapper.toGetResponseBody(pessoa.get());
                    return Collections.singletonList(pessoaGetResponseBody);
                }
            }

            if (codigoPessoa == null && login == null && status != null) {
                List<PessoaVo> listaDePessoasPorStatus = pessoaRepository.findAllByStatusOrderByCodigoPessoaDesc(status);
                if (!listaDePessoasPorStatus.isEmpty()) {
                    return listaDePessoasPorStatus.stream()
                            .map(pessoaMapper::toGetResponseBody)
                            .collect(Collectors.toList());
                }
            }
            List<PessoaVo> todasPessoas = pessoaRepository.findAll();
            if (!todasPessoas.isEmpty()) {
                return todasPessoas.stream()
                        .map(pessoaMapper::toGetResponseBody)
                        .sorted(Comparator.comparing(PessoaGetResponseBody::getCodigoPessoa).reversed())
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();

        }catch(ExcecaoPersonalizada ex){
            throw new ExcecaoPersonalizada("Não foi possível consultar pessoa. " + ex.getMessage());

        }catch(RuntimeException ex){
            throw new ExcecaoPersonalizada("Não foi possível consultar pessoa");
        }
    }
}
