package deal.service;

import core.dto.FinishRegistrationRequestDto;
import core.dto.ScoringDataDto;
import deal.persistence.model.Client;
import deal.persistence.model.Statement;
import core.dto.LoanOfferDto;
import core.type.ApplicationStatusType;
import java.util.UUID;

public interface LoanStatementService {

    Statement saveStatement(Client client);

    Statement saveStatement(Statement statement);

    Statement updateStatement(Statement statement, LoanOfferDto loanOfferDto, ApplicationStatusType statusType);

    Statement getStatementById(UUID statementId);

    Statement updateStatement(Statement statement, ApplicationStatusType statusType);

    ScoringDataDto buildScoringData(Client client, LoanOfferDto appliedOffer, FinishRegistrationRequestDto finishRegistrationRequestDto);
}
