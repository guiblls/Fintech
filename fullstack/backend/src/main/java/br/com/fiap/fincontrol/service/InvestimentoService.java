package br.com.fiap.fincontrol.service;
import br.com.fiap.fincontrol.model.*;
import br.com.fiap.fincontrol.dto.InvestimentoInput;
import br.com.fiap.fincontrol.repository.InvestimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.*;
import java.util.List;
@Service @Transactional
public class InvestimentoService {
 private final InvestimentoRepository repository; private final ContaService contas;
 public InvestimentoService(InvestimentoRepository r,ContaService c) {repository=r;contas=c;}
 public List<Investimento> listar() {return repository.findAll();}
 public Investimento buscar(Long id) {return repository.findById(id).orElseThrow(()->new ResponseStatusException(NOT_FOUND,"Investimento não encontrado"));}
 public Investimento criar(InvestimentoInput i) {Investimento v=new Investimento();preencher(v,i);return repository.save(v);}
 public Investimento atualizar(Long id,InvestimentoInput i) {Investimento v=buscar(id);preencher(v,i);return repository.save(v);}
 private void preencher(Investimento v,InvestimentoInput i) {
  Conta c=contas.buscar(i.contaId());
  if(!"ATIVA".equals(c.getStatusConta())&&"ATIVO".equals(i.statusInvestimento()))
   throw new ResponseStatusException(CONFLICT,"Investimento ativo exige uma conta ativa");
  if(i.dataAplicacao().isBefore(c.getDataAbertura())) throw new ResponseStatusException(BAD_REQUEST,"Aplicação não pode ser anterior à abertura da conta");
  v.setConta(c);v.setNomeInvestimento(i.nomeInvestimento().trim());v.setTipoInvestimento(i.tipoInvestimento());
  v.setValorAplicado(i.valorAplicado());v.setRentabilidade(i.rentabilidade());v.setDataAplicacao(i.dataAplicacao());v.setPrazo(i.prazo());v.setStatusInvestimento(i.statusInvestimento());
 }
 public void excluir(Long id) {repository.delete(buscar(id));}
}
