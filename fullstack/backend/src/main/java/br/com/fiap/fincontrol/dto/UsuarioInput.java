package br.com.fiap.fincontrol.dto;
import jakarta.validation.constraints.*;
public record UsuarioInput(
 @NotBlank @Size(max=120) String nome,
 @NotBlank @Email @Size(max=180) String email,
 @Size(max=128) String senha,
 @NotBlank @Pattern(regexp="[0-9]{11}", message="CPF deve conter 11 dígitos") String cpf,
 @Pattern(regexp="[0-9+() -]{0,20}", message="Telefone inválido") String telefone) {}
