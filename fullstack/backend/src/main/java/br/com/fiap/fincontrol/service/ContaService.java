package br.com.fiap.fincontrol.service;
import br.com.fiap.fincontrol.model.Conta;
import br.com.fiap.fincontrol.dto.ContaInput;
import br.com.fiap.fincontrol.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.*;
import java.util.List;
@Service @Transactional
public class ContaService {
 private final ContaRepository repository; private final UsuarioService usuarios; private final InvestimentoRepository investimentos;
 public ContaService(ContaRepository r,UsuarioService u,InvestimentoRepository i) {repository=r;usuarios=u;investimentos=i;}
 public List<Conta> listar() {return repository.findAll();}
 public Conta buscar(Long id) {return repository.findById(id).orElseThrow(()->new ResponseStatusException(NOT_FOUND,"Conta não encontrada"));}
 public Conta criar(ContaInput i) {Conta c=new Conta();preencher(c,i);return repository.save(c);}
 public Conta atualizar(Long id,ContaInput i) {
  Conta c=buscar(id);
  if(!c.getUsuario().getId().equals(i.usuarioId())&&investimentos.existsByContaId(id))
   throw new ResponseStatusException(CONFLICT,"Conta com investimentos não pode mudar de titular");
  if("INATIVA".equals(i.statusConta())&&investimentos.existsByContaIdAndStatusInvestimento(id,"ATIVO"))
   throw new ResponseStatusException(CONFLICT,"Resgate ou exclua os investimentos ativos antes de inativar a conta");
  if(investimentos.existsByContaIdAndDataAplicacaoBefore(id,i.dataAbertura()))
   throw new ResponseStatusException(CONFLICT,"Abertura não pode ser posterior às aplicações vinculadas");
  preencher(c,i);return repository.save(c);
 }
 private void preencher(Conta c,ContaInput i) {
  c.setUsuario(usuarios.buscar(i.usuarioId()));c.setSaldo(i.saldo());c.setTipoConta(i.tipoConta());
  c.setStatusConta(i.statusConta());c.setDataAbertura(i.dataAbertura());
 }
 public void excluir(Long id) {
  Conta c=buscar(id);
  if(investimentos.existsByContaId(id)) throw new ResponseStatusException(CONFLICT,"Exclua primeiro os investimentos desta conta");
  repository.delete(c);
 }
}
