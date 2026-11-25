package br.com.fiap.fincontrol.controller;
import br.com.fiap.fincontrol.model.Conta;
import br.com.fiap.fincontrol.dto.ContaInput;
import br.com.fiap.fincontrol.service.ContaService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/api/contas")
public class ContaController {
 private final ContaService service;
 public ContaController(ContaService service) {this.service=service;}
 @GetMapping public List<Conta> listar() {return service.listar();}
 @GetMapping("/{id}") public Conta buscar(@PathVariable Long id) {return service.buscar(id);}
 @PostMapping public ResponseEntity<Conta> criar(@Valid @RequestBody ContaInput input) {
  Conta salvo=service.criar(input);
  return ResponseEntity.created(URI.create("/api/contas/"+salvo.getId())).body(salvo);
 }
 @PutMapping("/{id}") public Conta atualizar(@PathVariable Long id,@Valid @RequestBody ContaInput input) {return service.atualizar(id,input);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> excluir(@PathVariable Long id,HttpSession session) {service.excluir(id);return ResponseEntity.noContent().build();}
}
