package br.com.fiap.fincontrol.repository;
import br.com.fiap.fincontrol.model.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InvestimentoRepository extends JpaRepository<Investimento, Long> { boolean existsByContaId(Long id);
 boolean existsByContaIdAndStatusInvestimento(Long id,String status);
 boolean existsByContaIdAndDataAplicacaoBefore(Long id,java.time.LocalDate data); }
