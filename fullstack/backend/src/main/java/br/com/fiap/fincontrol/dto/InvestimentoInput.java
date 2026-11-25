package br.com.fiap.fincontrol.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record InvestimentoInput(
 @NotNull @Positive Long contaId,
 @NotBlank @Size(max=120) String nomeInvestimento,
 @NotBlank @Pattern(regexp="CDB|TESOURO DIRETO|POUPANCA|FUNDO|ACOES|OUTROS") String tipoInvestimento,
 @NotNull @DecimalMin("0.01") @Digits(integer=13,fraction=2) BigDecimal valorAplicado,
 @NotNull @DecimalMin("-100.00") @Digits(integer=5,fraction=2) BigDecimal rentabilidade,
 @NotNull @PastOrPresent LocalDate dataAplicacao,
 @NotNull @Min(0) @Max(1200) Integer prazo,
 @NotBlank @Pattern(regexp="ATIVO|RESGATADO") String statusInvestimento) {}
