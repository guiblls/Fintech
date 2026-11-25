package br.com.fiap.fincontrol.config;
import br.com.fiap.fincontrol.repository.UsuarioRepository;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import java.io.IOException;
import java.util.Set;
@Configuration
public class SessionConfig implements WebMvcConfigurer {
 private final UsuarioRepository usuarios; private final String origin;
 public SessionConfig(UsuarioRepository usuarios,@Value("${app.frontend-origin}") String origin) {this.usuarios=usuarios;this.origin=origin;}
 @Override public void addInterceptors(InterceptorRegistry registry) {
  registry.addInterceptor(new HandlerInterceptor() {
   @Override public boolean preHandle(HttpServletRequest req,HttpServletResponse res,Object handler) throws IOException {
    res.setHeader("Cache-Control","no-store");
    if(!Set.of("GET","HEAD","OPTIONS").contains(req.getMethod())) {
     String from=req.getHeader("Origin");
     // Custom header blocks cross-origin form submissions; browsers cannot add it without CORS permission.
     if(!"FinControl".equals(req.getHeader("X-FinControl-Request"))||(from!=null&&!origin.equals(from)))
      return fail(res,403,"Origem da requisição não permitida");
    }
    if(req.getRequestURI().equals("/api/auth/login"))return true;
    HttpSession session=req.getSession(false);
    Long id=session==null ? null : (Long)session.getAttribute("usuarioId");
    if(id==null||!usuarios.existsById(id))return fail(res,401,"Faça login para continuar");
    return true;
   }
  }).addPathPatterns("/api/**");
 }
 private boolean fail(HttpServletResponse res,int code,String message) throws IOException {
  res.setStatus(code);res.setContentType("application/json;charset=UTF-8");res.getWriter().write("{\"message\":\""+message+"\"}");return false;
 }
}
