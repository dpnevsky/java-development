package deal.persistence.repository;

import deal.persistence.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {}
