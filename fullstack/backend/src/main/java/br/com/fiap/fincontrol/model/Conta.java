package br.com.fiap.fincontrol.model;
import jakarta.persistence.*;
@Entity
@Table(name="FC7_CONTA")
public class Conta {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FC7_CONTA_SEQ")
 @SequenceGenerator(name="FC7_CONTA_SEQ", sequenceName="FC7_CONTA_SEQ", allocationSize=1)
 private Long id;
 public Long getId() { return id; }
 public void setId(Long id) { this.id=id; }
 @Column(nullable=false, precision=15, scale=2)
 private java.math.BigDecimal saldo;
 public java.math.BigDecimal getSaldo() { return saldo; }
 public void setSaldo(java.math.BigDecimal value) { this.saldo=value; }
 @Column(name="tipo_conta", nullable=false, length=20)
 private String tipoConta;
 public String getTipoConta() { return tipoConta; }
 public void setTipoConta(String value) { this.tipoConta=value; }
 @Column(name="status_conta", nullable=false, length=10)
 private String statusConta;
 public String getStatusConta() { return statusConta; }
 public void setStatusConta(String value) { this.statusConta=value; }
 @Column(name="data_abertura", nullable=false)
 private java.time.LocalDate dataAbertura;
 public java.time.LocalDate getDataAbertura() { return dataAbertura; }
 public void setDataAbertura(java.time.LocalDate value) { this.dataAbertura=value; }
 @ManyToOne(optional=false)
 @JoinColumn(name="id_usuario", nullable=false)
 private Usuario usuario;
 public Usuario getUsuario() { return usuario; }
 public void setUsuario(Usuario value) { this.usuario=value; }
}
