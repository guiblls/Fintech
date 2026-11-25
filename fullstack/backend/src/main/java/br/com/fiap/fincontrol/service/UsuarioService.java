package br.com.fiap.fincontrol.service;
import br.com.fiap.fincontrol.model.Usuario;
import br.com.fiap.fincontrol.dto.UsuarioInput;
import br.com.fiap.fincontrol.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
@Service @Transactional
public class UsuarioService {
 private final UsuarioRepository repository; private final ContaRepository contas;
 public UsuarioService(UsuarioRepository repository,ContaRepository contas) {this.repository=repository;this.contas=contas;}
 public List<Usuario> listar() {return repository.findAll();}
 public Usuario buscar(Long id) {return repository.findById(id).orElseThrow(()->new ResponseStatusException(NOT_FOUND,"Usuário não encontrado"));}
 public Usuario criar(UsuarioInput input) {Usuario u=new Usuario();u.setDataCadastro(LocalDate.now());preencher(u,input);return repository.save(u);}
 public Usuario atualizar(Long id,UsuarioInput input) {Usuario u=buscar(id);preencher(u,input);return repository.save(u);}
 private void preencher(Usuario u,UsuarioInput i) {
  Long id=u.getId()==null ? -1L : u.getId();
  String email=i.email().trim().toLowerCase(Locale.ROOT);
  if(repository.existsByEmailIgnoreCaseAndIdNot(email,id)||repository.existsByCpfAndIdNot(i.cpf(),id))
   throw new ResponseStatusException(CONFLICT,"E-mail ou CPF já cadastrado");
  if(i.senha()!=null&&!i.senha().isBlank()) {
   if(i.senha().length()<8) throw new ResponseStatusException(BAD_REQUEST,"Senha deve ter pelo menos 8 caracteres");
   u.setSenhaHash(Senhas.hash(i.senha()));
  } else if(u.getId()==null) throw new ResponseStatusException(BAD_REQUEST,"Informe uma senha de pelo menos 8 caracteres");
  u.setNome(i.nome().trim());u.setEmail(email);u.setCpf(i.cpf());u.setTelefone(i.telefone());
 }
 public void excluir(Long id,Long autenticado) {
  Usuario u=buscar(id);
  if(id.equals(autenticado)) throw new ResponseStatusException(CONFLICT,"Não é possível excluir o usuário da sessão atual");
  if(contas.existsByUsuarioId(id)) throw new ResponseStatusException(CONFLICT,"Exclua primeiro as contas vinculadas ao usuário");
  repository.delete(u);
 }
}
