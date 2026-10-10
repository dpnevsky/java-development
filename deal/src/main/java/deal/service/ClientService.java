package deal.service;

import deal.persistence.model.Client;
import core.dto.LoanStatementRequestDto;
import java.util.UUID;

public interface ClientService {

    Client saveClient(LoanStatementRequestDto loanStatementRequestDto);

    String getEmailByStatementId(UUID statementId);
}
