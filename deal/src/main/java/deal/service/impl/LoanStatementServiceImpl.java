package deal.service.impl;

import core.dto.FinishRegistrationRequestDto;
import core.dto.ScoringDataDto;
import deal.persistence.model.Client;
import deal.persistence.model.Statement;
import deal.persistence.repository.StatementRepository;
import deal.service.LoanStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import core.dto.LoanOfferDto;
import core.dto.StatementStatusHistoryDto;
import core.type.ApplicationStatusType;
import core.type.ChangeType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanStatementServiceImpl implements LoanStatementService {

    private final StatementRepository statementRepository;

    @Override
    public Statement saveStatement(Client client) {
        Statement statement = new Statement();
        statement.setClientID(client);
        statement.setStatus(ApplicationStatusType.PREPARE_DOCUMENTS);
        statement.setCreationDate(LocalDateTime.now());
        List<StatementStatusHistoryDto> statusHistory = new LinkedList<>();
        statusHistory.add(StatementStatusHistoryDto.builder()
                .withTime(LocalDateTime.now())
                .withStatus(statement.getStatus())
                .withChangeType(ChangeType.AUTOMATIC)
                .build());
        statement.setStatementStatusHistory(statusHistory);

        return statementRepository.save(statement);
    }

    @Override
    public Statement saveStatement(Statement statement) {
        return statementRepository.save(statement);
    }

    @Override
    public Statement updateStatement(Statement statement, LoanOfferDto loanOfferDto, ApplicationStatusType statusType) {
        List<StatementStatusHistoryDto> statusHistory = new ArrayList<>(statement.getStatementStatusHistory());

        statement.setStatus(statusType);
        statusHistory.add(StatementStatusHistoryDto
                .builder()
                .withStatus(statement.getStatus())
                .withTime(LocalDateTime.now())
                .withChangeType(ChangeType.MANUAL)
                .build());

        statement.setStatementStatusHistory(statusHistory);
        statement.setAppliedOffer(loanOfferDto);
        return statement;
    }

    @Override
    public Statement updateStatement(Statement statement, ApplicationStatusType statusType) {
        List<StatementStatusHistoryDto> statusHistory = new ArrayList<>(statement.getStatementStatusHistory());

        statement.setStatus(statusType);
        statusHistory.add(StatementStatusHistoryDto
                .builder()
                .withStatus(statement.getStatus())
                .withTime(LocalDateTime.now())
                .withChangeType(ChangeType.MANUAL)
                .build());

        statement.setStatementStatusHistory(statusHistory);
        return statement;
    }


    @Override
    public ScoringDataDto buildScoringData(Client client, LoanOfferDto appliedOffer, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        return ScoringDataDto.builder()
                .withAmount(appliedOffer.getRequestedAmount())
                .withTerm(appliedOffer.getTerm())
                .withIsInsuranceEnabled(appliedOffer.getIsInsuranceEnabled())
                .withIsSalaryClient(appliedOffer.getIsSalaryClient())
                .withFirstName(client.getFirstName())
                .withLastName(client.getLastName())
                .withMiddleName(client.getMiddleName())
                .withBirthdate(client.getBirthDate())
                .withPassportSeries(client.getPassport().getPassportSeries())
                .withPassportNumber(client.getPassport().getPassportNumber())
                .withGender(finishRegistrationRequestDto.gender())
                .withMaritalStatus(finishRegistrationRequestDto.maritalStatus())
                .withDependentAmount(finishRegistrationRequestDto.dependentAmount())
                .withPassportIssueDate(finishRegistrationRequestDto.passportIssueDate())
                .withPassportIssueBranch(finishRegistrationRequestDto.passportIssueBranch())
                .withEmployment(finishRegistrationRequestDto.employment())
                .withAccountNumber(finishRegistrationRequestDto.accountNumber())
                .build();
    }

//    @Transactional
    @Override
    public void updateStatementStatus(UUID statementId, ApplicationStatusType applicationStatusType) {
        statementRepository.updateStatementStatusAndStatusHistory(statementId, applicationStatusType.toString());
    }

//    @Transactional
    @Override
    public void setSesCode(UUID statementId, UUID sesCode) {
        statementRepository.setSesCodeAndSignDate(statementId, sesCode);
    }

    @Override
    public UUID getSesCodeByStatementId(UUID statementId) {
        return statementRepository.findSesCodeByStatementId(statementId);
    }

    @Override
    public Statement getStatementById(UUID statementId) {
        return statementRepository.findById(statementId)
                .orElseThrow(() -> new IllegalArgumentException("Statement not found"));
    }
}
