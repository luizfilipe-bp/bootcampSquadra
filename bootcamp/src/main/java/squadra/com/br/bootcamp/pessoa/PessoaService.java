package squadra.com.br.bootcamp.pessoa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squadra.com.br.bootcamp.bairro.BairroGetResponseBody;
import squadra.com.br.bootcamp.bairro.BairroService;
import squadra.com.br.bootcamp.bairro.BairroVo;
import squadra.com.br.bootcamp.endereco.EnderecoGetResponseBody;
import squadra.com.br.bootcamp.endereco.EnderecoService;
import squadra.com.br.bootcamp.endereco.EnderecoVo;
import squadra.com.br.bootcamp.exception.ExcecaoPersonalizadaException;
import squadra.com.br.bootcamp.exception.RegistroJaExisteNoBancoException;
import squadra.com.br.bootcamp.exception.RegistroNaoExisteNoBancoException;
import squadra.com.br.bootcamp.municipio.MunicipioGetResponseBody;
import squadra.com.br.bootcamp.municipio.MunicipioService;
import squadra.com.br.bootcamp.municipio.MunicipioVo;
import squadra.com.br.bootcamp.uf.UfService;
import squadra.com.br.bootcamp.uf.UfGetResponseBody;
import squadra.com.br.bootcamp.uf.UfVo;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {
    private final EnderecoService enderecoService;
    private final BairroService bairroService;
    private final MunicipioService municipioService;
    private final UfService ufService;

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
            return listarTodasPessoas();

        }catch(ExcecaoPersonalizadaException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível consultar pessoa. " + ex.getMessage());

        }catch(RuntimeException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível consultar pessoa");
        }
    }

    @Transactional
    public List<PessoaGetResponseBody> save(PessoaPostRequestBody pessoaPostRequestBody){
        try{
            verificaExisteLoginCadastrado(pessoaPostRequestBody.getLogin());
            enderecoService.verificaEnderecosValidos(pessoaPostRequestBody.getEnderecos());

            PessoaVo pessoa = pessoaMapper.toPessoaVo(pessoaPostRequestBody);
            pessoaRepository.save(pessoa);
            pessoaPostRequestBody.getEnderecos().forEach(enderecoPostRequestBody -> enderecoPostRequestBody.setCodigoPessoa(pessoa.getCodigoPessoa()));
            enderecoService.save(pessoaPostRequestBody.getEnderecos().stream().map(enderecoService::converterParaEnderecoVo).toList());

        }catch(ExcecaoPersonalizadaException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar a pessoa. " + ex.getMessage());
        }catch(Exception ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar a pessoa.");
        }
        return listarTodasPessoas();
    }

    @Transactional
    public List<PessoaGetResponseBody> update(PessoaPutRequestBody pessoa){
        try {
            verificaNaoExisteCodigoPessoaCadastrado(pessoa.getCodigoPessoa());

            Optional<PessoaVo> pessoaBanco = pessoaRepository.findById(pessoa.getCodigoPessoa());
            if(pessoaBanco.isPresent()){
                if(!pessoaBanco.get().getLogin().equals(pessoa.getLogin())){
                    verificaExisteLoginCadastrado(pessoa.getLogin());
                }
                PessoaVo pessoaAlterada = pessoaMapper.toPessoaVo(pessoa);
                pessoaRepository.save(pessoaAlterada);
            }

            List<EnderecoVo> enderecosSeraoAlterados = pessoa.getEnderecos().stream()
                    .map(enderecoService::converterParaEnderecoVo).toList();

            List<EnderecoVo> enderecosAntigos = enderecoService.buscarEnderecosPorCodigoPessoa(pessoa.getCodigoPessoa());

            Map<Long, EnderecoVo> enderecoVoMap = enderecosSeraoAlterados.stream()
                    .collect(Collectors.toMap(EnderecoVo::getCodigoEndereco, endereco -> endereco));

            List<EnderecoVo> enderecosParaRemover = enderecosAntigos.stream()
                    .filter(endereco -> !enderecoVoMap.containsKey(endereco.getCodigoEndereco()))
                    .toList();

            enderecoService.save(enderecosSeraoAlterados);
            enderecoService.delete(enderecosParaRemover);
            return listarTodasPessoas();

        }catch(ExcecaoPersonalizadaException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível alterar pessoa. " + ex.getMessage());
        }catch(Exception ex){
            throw new ExcecaoPersonalizadaException("Não foi possível alterar pessoa.");
        }

    }

    private void verificaExisteLoginCadastrado(String login) throws RegistroJaExisteNoBancoException {
        if(pessoaRepository.findByLogin(login).isPresent()){
            throw new RegistroJaExisteNoBancoException("Já existe um login '" + login + "' cadastrado no banco de dados");
        }
    }

    private void verificaNaoExisteCodigoPessoaCadastrado(Long codigoPessoa){
        if(!pessoaRepository.existsById(codigoPessoa)){
            throw new RegistroNaoExisteNoBancoException("Não existe pessoa com codigoPessoa " + codigoPessoa);
        }
    }

    private List<PessoaGetResponseBody> listarTodasPessoas(){
        List<PessoaVo> todasPessoas = pessoaRepository.findAll();
        if (!todasPessoas.isEmpty()) {
            return todasPessoas.stream()
                    .map(pessoaMapper::toGetResponseBody)
                    .sorted(Comparator.comparing(PessoaGetResponseBody::getCodigoPessoa).reversed())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
