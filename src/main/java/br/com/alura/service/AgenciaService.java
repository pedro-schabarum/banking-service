package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AgenciaService {
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private final AgenciaRepository agenciaRepository;

    AgenciaService(AgenciaRepository agenciaRepository){
        this.agenciaRepository = agenciaRepository;
    }
    public void cadastrar(Agencia agencia){
        AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
        System.out.println("agenciaHttp " + agenciaHttp);
        System.out.println("agencia " + agencia);
        if(agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)){
            agenciaRepository.persist(agencia);
            Log.info("Agencia com cnpj " + agencia.getCnpj() + " foi adicionada");
        }else{
            Log.info("Agencia com cnpj " + agencia + " não foi adicionada");
            throw new AgenciaNaoAtivaOuNaoEncontradaException();
        }
    }

    public Agencia buscarPorId(Long id){
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id){
        agenciaRepository.deleteById(id);
        Log.info("Agencia com id " + id + " foi deletada");
    }

    public void alterar(Agencia agencia){
        agenciaRepository.update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4", agencia.getNome(), agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getId());
        Log.info("Agencia com cnpj " + agencia.getCnpj() + " foi alterada");
    }
}
