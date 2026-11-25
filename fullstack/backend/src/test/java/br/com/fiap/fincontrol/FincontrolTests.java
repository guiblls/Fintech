package br.com.fiap.fincontrol;
import br.com.fiap.fincontrol.service.*;
import br.com.fiap.fincontrol.model.*;
import br.com.fiap.fincontrol.dto.*;
import br.com.fiap.fincontrol.repository.*;
import br.com.fiap.fincontrol.controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class FincontrolTests {
 @Test void senhaTemSaltEValidaSemTextoPuro() {
  String a=Senhas.hash("FinControl@123"), b=Senhas.hash("FinControl@123");
  assertNotEquals(a,b);assertTrue(Senhas.confere("FinControl@123",a));assertFalse(Senhas.confere("errada",a));assertFalse(Senhas.confere("senha","formato-invalido"));
 }
 @Test void bloqueiaExclusaoDeUsuarioComConta() {
  var r=mock(UsuarioRepository.class);var c=mock(ContaRepository.class);
  when(r.findById(2L)).thenReturn(Optional.of(new Usuario()));when(c.existsByUsuarioId(2L)).thenReturn(true);
  var e=assertThrows(ResponseStatusException.class,()->new UsuarioService(r,c).excluir(2L,1L));assertEquals(409,e.getStatusCode().value());verify(r,never()).delete(any());
 }
 @Test void bloqueiaAutoExclusao() {
  var r=mock(UsuarioRepository.class);when(r.findById(1L)).thenReturn(Optional.of(new Usuario()));
  assertThrows(ResponseStatusException.class,()->new UsuarioService(r,mock(ContaRepository.class)).excluir(1L,1L));verify(r,never()).delete(any());
 }
 @Test void emailDuplicadoRetornaConflito() {
  var r=mock(UsuarioRepository.class);when(r.existsByEmailIgnoreCaseAndIdNot("teste@exemplo.com",-1L)).thenReturn(true);
  var input=new UsuarioInput("Teste","TESTE@EXEMPLO.COM","senha1234","12345678901","");
  assertEquals(409,assertThrows(ResponseStatusException.class,()->new UsuarioService(r,mock(ContaRepository.class)).criar(input)).getStatusCode().value());
 }
 @Test void investimentoEmContaInativaEhRejeitado() {
  var c=mock(ContaService.class);var conta=new Conta();conta.setStatusConta("INATIVA");when(c.buscar(1L)).thenReturn(conta);
  var input=new InvestimentoInput(1L,"CDB","CDB",new BigDecimal("100"),BigDecimal.TEN,LocalDate.now(),12,"ATIVO");
  var r=mock(InvestimentoRepository.class);
  assertEquals(409,assertThrows(ResponseStatusException.class,()->new InvestimentoService(r,c).criar(input)).getStatusCode().value());verify(r,never()).save(any());
 }
 @Test void impedeInativarContaComInvestimentoAtivo() {
  var r=mock(ContaRepository.class);var i=mock(InvestimentoRepository.class);var u=new Usuario();u.setId(1L);var c=new Conta();c.setUsuario(u);when(r.findById(1L)).thenReturn(Optional.of(c));when(i.existsByContaIdAndStatusInvestimento(1L,"ATIVO")).thenReturn(true);
  var input=new ContaInput(1L,BigDecimal.ZERO,"CORRENTE","INATIVA",LocalDate.now());
  assertThrows(ResponseStatusException.class,()->new ContaService(r,mock(UsuarioService.class),i).atualizar(1L,input));verify(r,never()).save(any());
 }
 @Test void controllerRespeita201Location200204E404() throws Exception {
  var s=mock(ContaService.class);var c=new Conta();c.setId(7L);when(s.criar(any())).thenReturn(c);when(s.buscar(7L)).thenReturn(c);when(s.atualizar(eq(7L),any())).thenReturn(c);when(s.buscar(99L)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Conta não encontrada"));
  MockMvc mvc=MockMvcBuilders.standaloneSetup(new ContaController(s)).setControllerAdvice(new ApiExceptionHandler()).build();
  String json="{\"usuarioId\":1,\"saldo\":0,\"tipoConta\":\"CORRENTE\",\"statusConta\":\"ATIVA\",\"dataAbertura\":\"2025-01-01\"}";
  mvc.perform(post("/api/contas").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andExpect(header().string("Location","/api/contas/7"));
  mvc.perform(get("/api/contas/7")).andExpect(status().isOk());
  mvc.perform(put("/api/contas/7").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
  mvc.perform(delete("/api/contas/7")).andExpect(status().isNoContent()).andExpect(content().string(""));
  mvc.perform(get("/api/contas/99")).andExpect(status().isNotFound());
  mvc.perform(post("/api/contas").contentType(MediaType.APPLICATION_JSON).content(json.replace("\"saldo\":0","\"saldo\":-1"))).andExpect(status().isBadRequest());
 }
 @Test void loginValidoCriaSessaoESenhaNaoVaza() throws Exception {
  var r=mock(UsuarioRepository.class);var u=new Usuario();u.setId(1L);u.setNome("Teste");u.setSenhaHash(Senhas.hash("senha1234"));when(r.findByEmailIgnoreCase("teste@exemplo.com")).thenReturn(Optional.of(u));
  MockMvc mvc=MockMvcBuilders.standaloneSetup(new AuthController(r)).setControllerAdvice(new ApiExceptionHandler()).build();
  var result=mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"teste@exemplo.com\",\"senha\":\"senha1234\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.senhaHash").doesNotExist()).andReturn();
  assertEquals(1L,result.getRequest().getSession().getAttribute("usuarioId"));
  mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"teste@exemplo.com\",\"senha\":\"errada\"}")).andExpect(status().isUnauthorized());
 }
}
