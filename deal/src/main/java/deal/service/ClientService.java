package deal.service;

import deal.persistence.model.Client;
import core.dto.LoanStatementRequestDto;

public interface ClientService {

    Client saveClient(LoanStatementRequestDto loanStatementRequestDto);
}
