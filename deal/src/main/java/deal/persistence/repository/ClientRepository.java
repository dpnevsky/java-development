package deal.persistence.repository;

import deal.persistence.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("SELECT c.email " +
            "FROM Statement s " +
            "JOIN s.clientID c " +
            "WHERE s.statementId = :statementId")
    String findEmailByStatementId(@Param("statementId") UUID statementId);
}
