package deal.persistence.repository;

import deal.persistence.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, UUID> {}
