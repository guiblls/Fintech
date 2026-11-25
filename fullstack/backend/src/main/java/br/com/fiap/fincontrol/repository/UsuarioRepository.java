package br.com.fiap.fincontrol.repository;
import br.com.fiap.fincontrol.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { java.util.Optional<Usuario> findByEmailIgnoreCase(String email);
 boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
 boolean existsByCpfAndIdNot(String cpf, Long id); }
