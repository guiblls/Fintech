package br.com.fiap.fincontrol.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(ResponseStatusException.class) public ResponseEntity<?> status(ResponseStatusException e) {return ResponseEntity.status(e.getStatusCode()).body(Map.of("message",e.getReason()==null?"Operação não permitida":e.getReason()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<?> validation(MethodArgumentNotValidException e) {
  var errors=e.getBindingResult().getFieldErrors().stream().map(f->f.getField()+": "+f.getDefaultMessage()).toList();
  return ResponseEntity.badRequest().body(Map.of("message","Verifique os campos informados","errors",errors));
 }
 @ExceptionHandler({HttpMessageNotReadableException.class,MethodArgumentTypeMismatchException.class}) public ResponseEntity<?> formato(Exception e) {return ResponseEntity.badRequest().body(Map.of("message","Dados em formato inválido"));}
 @ExceptionHandler(DataIntegrityViolationException.class) public ResponseEntity<?> conflito(Exception e) {return ResponseEntity.status(409).body(Map.of("message","Registro duplicado ou vinculado a outro cadastro"));}
}
