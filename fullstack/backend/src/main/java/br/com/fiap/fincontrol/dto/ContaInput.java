package br.com.fiap.fincontrol.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record ContaInput(
 @NotNull @Positive Long usuarioId,
 @NotNull @DecimalMin("0.00") @Digits(integer=13,fraction=2) BigDecimal saldo,
 @NotBlank @Pattern(regexp="CORRENTE|POUPANCA|DINHEIRO") String tipoConta,
 @NotBlank @Pattern(regexp="ATIVA|INATIVA") String statusConta,
 @NotNull @PastOrPresent LocalDate dataAbertura) {}
