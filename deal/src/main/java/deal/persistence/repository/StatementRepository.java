package deal.persistence.repository;

import deal.persistence.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, UUID> {

    @Modifying
    @Query(value = """
UPDATE statement
SET status = :status,
    status_history = coalesce(status_history, '[]'::jsonb) || to_jsonb(json_build_object(
        'status', :status,
        'time', now(),
        'changeType', 'AUTOMATIC'
    ))
WHERE statement_id = :statementId
""", nativeQuery = true)
    void updateStatementStatusAndStatusHistory(@Param("statementId") UUID statementId, @Param("status") String status);

    @Modifying
    @Query(value = """
UPDATE statement
SET ses_code = :sesCode,
    sign_date = now()
WHERE statement_id = :statementId
""", nativeQuery = true)
    void setSesCodeAndSignDate(@Param("statementId") UUID statementId, @Param("sesCode") UUID sesCode);

    @Query("SELECT s.sesCode FROM Statement s WHERE s.statementId = :statementId")
    UUID findSesCodeByStatementId(@Param("statementId") UUID statementId);
}
