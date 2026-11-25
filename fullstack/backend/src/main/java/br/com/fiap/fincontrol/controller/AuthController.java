package br.com.fiap.fincontrol.controller;
import br.com.fiap.fincontrol.repository.UsuarioRepository;
import br.com.fiap.fincontrol.model.Usuario;
import br.com.fiap.fincontrol.service.Senhas;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final UsuarioRepository usuarios;
 // Constant dummy hash keeps nonexistent-user and incorrect-password paths comparable.
 private static final String DUMMY=Senhas.hash("senha-inexistente");
 public AuthController(UsuarioRepository usuarios) {this.usuarios=usuarios;}
 public record Login(@NotBlank @Email @Size(max=180) String email,@NotBlank @Size(max=128) String senha) {}
 @PostMapping("/login") public Usuario login(@Valid @RequestBody Login login,HttpServletRequest request) {
  Usuario u=usuarios.findByEmailIgnoreCase(login.email().trim()).orElse(null);
  boolean ok=Senhas.confere(login.senha(),u==null ? DUMMY : u.getSenhaHash());
  if(!ok||u==null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"E-mail ou senha inválidos");
  HttpSession antiga=request.getSession(false);if(antiga!=null) antiga.invalidate();
  request.getSession(true).setAttribute("usuarioId",u.getId());return u;
 }
 @GetMapping("/me") public Usuario me(HttpSession s) {
  return usuarios.findById((Long)s.getAttribute("usuarioId")).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sessão expirada"));
 }
 @PostMapping("/logout") public ResponseEntity<Void> logout(HttpServletRequest request) {
  HttpSession s=request.getSession(false);if(s!=null)s.invalidate();return ResponseEntity.noContent().build();
 }
}
