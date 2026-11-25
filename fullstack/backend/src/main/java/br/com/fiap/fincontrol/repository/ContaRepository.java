package br.com.fiap.fincontrol.repository;
import br.com.fiap.fincontrol.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContaRepository extends JpaRepository<Conta, Long> { boolean existsByUsuarioId(Long id); }
