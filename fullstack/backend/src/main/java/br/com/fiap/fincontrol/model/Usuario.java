package br.com.fiap.fincontrol.model;
import jakarta.persistence.*;
@Entity
@Table(name="FC7_USUARIO")
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FC7_USUARIO_SEQ")
 @SequenceGenerator(name="FC7_USUARIO_SEQ", sequenceName="FC7_USUARIO_SEQ", allocationSize=1)
 private Long id;
 public Long getId() { return id; }
 public void setId(Long id) { this.id=id; }
 @Column(nullable=false, length=120)
 private String nome;
 public String getNome() { return nome; }
 public void setNome(String value) { this.nome=value; }
 @Column(nullable=false, unique=true, length=180)
 private String email;
 public String getEmail() { return email; }
 public void setEmail(String value) { this.email=value; }
 @com.fasterxml.jackson.annotation.JsonIgnore
 @Column(name="senha_hash", nullable=false, length=255)
 private String senhaHash;
 public String getSenhaHash() { return senhaHash; }
 public void setSenhaHash(String value) { this.senhaHash=value; }
 @Column(nullable=false, unique=true, length=11)
 private String cpf;
 public String getCpf() { return cpf; }
 public void setCpf(String value) { this.cpf=value; }
 @Column(length=20)
 private String telefone;
 public String getTelefone() { return telefone; }
 public void setTelefone(String value) { this.telefone=value; }
 @Column(name="data_cadastro", nullable=false)
 private java.time.LocalDate dataCadastro;
 public java.time.LocalDate getDataCadastro() { return dataCadastro; }
 public void setDataCadastro(java.time.LocalDate value) { this.dataCadastro=value; }
}
