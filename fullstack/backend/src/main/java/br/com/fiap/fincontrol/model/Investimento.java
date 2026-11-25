package br.com.fiap.fincontrol.model;
import jakarta.persistence.*;
@Entity
@Table(name="FC7_INVESTIMENTO")
public class Investimento {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FC7_INVESTIMENTO_SEQ")
 @SequenceGenerator(name="FC7_INVESTIMENTO_SEQ", sequenceName="FC7_INVESTIMENTO_SEQ", allocationSize=1)
 private Long id;
 public Long getId() { return id; }
 public void setId(Long id) { this.id=id; }
 @ManyToOne(optional=false)
 @JoinColumn(name="id_conta", nullable=false)
 private Conta conta;
 public Conta getConta() { return conta; }
 public void setConta(Conta value) { this.conta=value; }
 @Column(name="nome_investimento", nullable=false, length=120)
 private String nomeInvestimento;
 public String getNomeInvestimento() { return nomeInvestimento; }
 public void setNomeInvestimento(String value) { this.nomeInvestimento=value; }
 @Column(name="tipo_investimento", nullable=false, length=40)
 private String tipoInvestimento;
 public String getTipoInvestimento() { return tipoInvestimento; }
 public void setTipoInvestimento(String value) { this.tipoInvestimento=value; }
 @Column(name="valor_aplicado", nullable=false, precision=15, scale=2)
 private java.math.BigDecimal valorAplicado;
 public java.math.BigDecimal getValorAplicado() { return valorAplicado; }
 public void setValorAplicado(java.math.BigDecimal value) { this.valorAplicado=value; }
 @Column(nullable=false, precision=7, scale=2)
 private java.math.BigDecimal rentabilidade;
 public java.math.BigDecimal getRentabilidade() { return rentabilidade; }
 public void setRentabilidade(java.math.BigDecimal value) { this.rentabilidade=value; }
 @Column(name="data_aplicacao", nullable=false)
 private java.time.LocalDate dataAplicacao;
 public java.time.LocalDate getDataAplicacao() { return dataAplicacao; }
 public void setDataAplicacao(java.time.LocalDate value) { this.dataAplicacao=value; }
 @Column(nullable=false)
 private Integer prazo;
 public Integer getPrazo() { return prazo; }
 public void setPrazo(Integer value) { this.prazo=value; }
 @Column(name="status_investimento", nullable=false, length=12)
 private String statusInvestimento;
 public String getStatusInvestimento() { return statusInvestimento; }
 public void setStatusInvestimento(String value) { this.statusInvestimento=value; }
}
