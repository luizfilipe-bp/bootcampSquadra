package squadra.com.br.bootcamp.pessoa;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import squadra.com.br.bootcamp.bairro.BairroGetRequestBody;
import squadra.com.br.bootcamp.bairro.BairroService;
import squadra.com.br.bootcamp.bairro.BairroVo;
import squadra.com.br.bootcamp.endereco.EnderecoGetRequestBody;
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
@Validated
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
            List<PessoaGetRequestBody> pessoasFiltradas = filtrarEOrdenarPessoas(codigoPessoa, login, status);
            if (codigoPessoa != null && !pessoasFiltradas.isEmpty()) {
                List<EnderecoVo> enderecosDaPessoa = enderecoService.buscarEnderecosPorCodigoPessoa(codigoPessoa);

                List<EnderecoGetRequestBody> enderecosGetResponseBody = enderecosDaPessoa.stream().map(enderecoVo -> {
                    BairroVo bairro = bairroService.buscarBairroPorCodigoBairro(enderecoVo.getCodigoBairro());
                    MunicipioVo municipio = municipioService.buscarMunicipioPorCodigoMunicipio(bairro.getCodigoMunicipio());
                    UfVo uf = ufService.buscarUfPorCodigoUf(municipio.getCodigoUF());

                    UfGetResponseBody ufResponse = ufService.converterUfVoParaGetResponseBody(uf);
                    MunicipioGetResponseBody municipioResponse = municipioService.converterMunicipioParaGetResponseBody(municipio);
                    BairroGetRequestBody bairroResponse = bairroService.converterBairroVoParaGetResponseBody(bairro);
                    EnderecoGetRequestBody enderecoResponse = enderecoService.converterEnderecoVoParaGetResponseBody(enderecoVo);

                    municipioResponse.setUf(ufResponse);
                    bairroResponse.setMunicipio(municipioResponse);
                    enderecoResponse.setBairro(bairroResponse);

                    return enderecoResponse;
                }).toList();

                pessoasFiltradas.getFirst().setEnderecos(enderecosGetResponseBody.reversed());

                return pessoasFiltradas.getFirst();
            }
            return pessoasFiltradas;

        }catch(ExcecaoPersonalizadaException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível consultar pessoa. " + ex.getMessage());

        }catch(RuntimeException ex){
            throw new ExcecaoPersonalizadaException("Não foi possível consultar pessoa");
        }
    }

    @Transactional
    public List<PessoaGetRequestBody> save(PessoaPostRequestBody pessoaPostRequestBody){
        try{
            verificaExisteLoginCadastrado(pessoaPostRequestBody.getLogin());
            pessoaPostRequestBody.getEnderecos().forEach(endereco -> bairroService.verificaExisteBairroCadastrado(endereco.getCodigoBairro()));
            List<EnderecoVo> enderecos = pessoaPostRequestBody.getEnderecos().stream().map(enderecoService::converterParaEnderecoVo).toList();

            PessoaVo pessoa = pessoaMapper.toPessoaVo(pessoaPostRequestBody);
            pessoaRepository.save(pessoa);

            enderecos.forEach(endereco -> endereco.setCodigoPessoa(pessoa.getCodigoPessoa()));
            enderecoService.save(enderecos);

        }catch(ExcecaoPersonalizadaException ex) {
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar a pessoa. " + ex.getMessage());

        }catch(ConstraintViolationException ex) {
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar a pessoa. Houve um problema no endereço: " + ex.getConstraintViolations().iterator().next().getMessage());

        }catch(Exception ex){
            throw new ExcecaoPersonalizadaException("Não foi possível cadastrar a pessoa.");
        }
        return filtrarEOrdenarPessoas(null, null, null);
    }

    @Transactional
    public List<PessoaGetRequestBody> update(PessoaPutRequestBody pessoa){
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

            pessoa.getEnderecos().forEach(endereco ->
            {   bairroService.verificaExisteBairroCadastrado(endereco.getCodigoBairro());
                if(endereco.getCodigoEndereco() != null){
                    enderecoService.verificaExisteEnderecoCadastrado(endereco.getCodigoEndereco());
                }
            });

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
            return filtrarEOrdenarPessoas(null, null, null);

        }catch(ExcecaoPersonalizadaException ex) {
            throw new ExcecaoPersonalizadaException("Não foi possível alterar pessoa. " + ex.getMessage());

        }catch(ConstraintViolationException ex) {
            throw new ExcecaoPersonalizadaException("Não foi possível alterar pessoa. Houve um problema no endereço: " + ex.getConstraintViolations().iterator().next().getMessage());

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new ExcecaoPersonalizadaException("Não foi possível alterar pessoa.");
        }

    }

    private void verificaExisteLoginCadastrado(String login) throws RegistroJaExisteNoBancoException {
        if(pessoaRepository.findByLogin(login).isPresent()){
            throw new RegistroJaExisteNoBancoException("Já existe um login '" + login + "' cadastrado no banco de dados");
        }
    }

    private void verificaNaoExisteCodigoPessoaCadastrado(Long codigoPessoa) throws RegistroNaoExisteNoBancoException{
        if(!pessoaRepository.existsById(codigoPessoa)){
            throw new RegistroNaoExisteNoBancoException("Não existe pessoa com codigoPessoa " + codigoPessoa);
        }
    }

    private List<PessoaGetRequestBody> filtrarEOrdenarPessoas(Long codigoPessoa, String login, Integer status){
        return pessoaRepository.findAll().stream()
            .filter(pessoaVo -> codigoPessoa == null || pessoaVo.getCodigoPessoa().equals(codigoPessoa))
            .filter(pessoaVo -> login == null || login.isEmpty() || pessoaVo.getLogin().equals(login))
            .filter(pessoaVo -> status == null || pessoaVo.getStatus().equals(status))
            .map(pessoaMapper::toGetResponseBody)
            .sorted(Comparator.comparing(PessoaGetRequestBody::getCodigoPessoa).reversed())
            .collect(Collectors.toList());
    }
}
