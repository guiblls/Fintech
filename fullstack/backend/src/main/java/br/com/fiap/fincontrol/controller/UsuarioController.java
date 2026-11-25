package br.com.fiap.fincontrol.controller;
import br.com.fiap.fincontrol.model.Usuario;
import br.com.fiap.fincontrol.dto.UsuarioInput;
import br.com.fiap.fincontrol.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/api/usuarios")
public class UsuarioController {
 private final UsuarioService service;
 public UsuarioController(UsuarioService service) {this.service=service;}
 @GetMapping public List<Usuario> listar() {return service.listar();}
 @GetMapping("/{id}") public Usuario buscar(@PathVariable Long id) {return service.buscar(id);}
 @PostMapping public ResponseEntity<Usuario> criar(@Valid @RequestBody UsuarioInput input) {
  Usuario salvo=service.criar(input);
  return ResponseEntity.created(URI.create("/api/usuarios/"+salvo.getId())).body(salvo);
 }
 @PutMapping("/{id}") public Usuario atualizar(@PathVariable Long id,@Valid @RequestBody UsuarioInput input) {return service.atualizar(id,input);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> excluir(@PathVariable Long id,HttpSession session) {service.excluir(id,(Long)session.getAttribute("usuarioId"));return ResponseEntity.noContent().build();}
}
