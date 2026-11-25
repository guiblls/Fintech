package br.com.fiap.fincontrol.controller;
import br.com.fiap.fincontrol.model.Investimento;
import br.com.fiap.fincontrol.dto.InvestimentoInput;
import br.com.fiap.fincontrol.service.InvestimentoService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/api/investimentos")
public class InvestimentoController {
 private final InvestimentoService service;
 public InvestimentoController(InvestimentoService service) {this.service=service;}
 @GetMapping public List<Investimento> listar() {return service.listar();}
 @GetMapping("/{id}") public Investimento buscar(@PathVariable Long id) {return service.buscar(id);}
 @PostMapping public ResponseEntity<Investimento> criar(@Valid @RequestBody InvestimentoInput input) {
  Investimento salvo=service.criar(input);
  return ResponseEntity.created(URI.create("/api/investimentos/"+salvo.getId())).body(salvo);
 }
 @PutMapping("/{id}") public Investimento atualizar(@PathVariable Long id,@Valid @RequestBody InvestimentoInput input) {return service.atualizar(id,input);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> excluir(@PathVariable Long id,HttpSession session) {service.excluir(id);return ResponseEntity.noContent().build();}
}
