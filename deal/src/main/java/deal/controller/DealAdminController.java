package deal.controller;

import deal.persistence.model.Statement;
import deal.service.LoanStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@Tag(name = "Admin Controller", description = "for administrative functions")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/deal/admin")
public class DealAdminController {

    private final LoanStatementService loanStatementService;

    @Operation(summary = "Get statement by ID")
    @GetMapping("/statement/{statementId}")
    public ResponseEntity<Statement> getStatementById(@PathVariable(name = "statementId") UUID statementId) {
        log.info("Received request to get statement by ID: {}", statementId);

        Statement statement = loanStatementService.getStatementById(statementId);
        if (statement == null) {
            log.warn("Statement with ID {} not found", statementId);
            return ResponseEntity.notFound().build();
        }

        log.info("Statement with ID {} retrieved successfully", statementId);
        return ResponseEntity.ok(statement);
    }

    @Operation(summary = "Get all statements")
    @GetMapping("/statement")
    public ResponseEntity<List<Statement>> getAllStatements() {
        log.info("Received request to get all statements");

        List<Statement> statements = loanStatementService.getAllStatements();
        if (statements.isEmpty()) {
            log.info("No statements found");
            return ResponseEntity.noContent().build();
        }

        log.info("Retrieved {} statements successfully", statements.size());
        return ResponseEntity.ok(statements);
    }
}
