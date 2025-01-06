package deal.service.impl;

import core.dto.EmploymentDto;
import core.dto.FinishRegistrationRequestDto;
import core.dto.LoanOfferDto;
import core.dto.ScoringDataDto;
import core.dto.StatementStatusHistoryDto;
import core.type.EmploymentStatusType;
import core.type.GenderType;
import core.type.MaritalStatusType;
import core.type.PositionType;
import core.util.Passport;
import deal.persistence.model.Client;
import deal.persistence.model.Credit;
import deal.persistence.model.Statement;
import deal.persistence.repository.StatementRepository;
import core.type.ApplicationStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanStatementServiceImplTest {

    @Mock
    private StatementRepository statementRepository;

    @InjectMocks
    private LoanStatementServiceImpl loanStatementService;

    private static Credit credit;
    private Statement statement;
    private Client client;
    private LoanOfferDto loanOffer;
    private FinishRegistrationRequestDto finishRegistrationRequestDto;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .withClientId(UUID.randomUUID())
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withBirthDate(LocalDate.of(1990, 5, 10))
                .withEmail("ivan.ivanov@yandex.ru")
                .withGender(GenderType.MALE)
                .withMaritalStatus(MaritalStatusType.MARRIED)
                .withDependentAmount(2)
                .withPassport(Passport.builder()
                        .withPassportSeries("1234")
                        .withPassportNumber("654321")
                        .build())
                .withEmployment(EmploymentDto.builder()
                        .withEmploymentStatus(EmploymentStatusType.STUDENT)
                        .withPosition(PositionType.OTHER)
                        .withSalary(BigDecimal.valueOf(60000))
                        .build())
                .withAccountNumber("1234567890")
                .build();

        loanOffer = LoanOfferDto.builder()
                .withStatementId(UUID.randomUUID())
                .withRequestedAmount(BigDecimal.valueOf(25000.00))
                .withTotalAmount(BigDecimal.valueOf(30000.00))
                .withTerm(12)
                .withMonthlyPayment(BigDecimal.valueOf(2500.00))
                .withRate(BigDecimal.valueOf(5.5))
                .withIsInsuranceEnabled(true)
                .withIsSalaryClient(true)
                .build();

        credit = Credit.builder()
                .withCreditId(UUID.randomUUID())
                .withAmount(BigDecimal.valueOf(25000.00))
                .withTerm(12)
                .withMonthlyPayment(BigDecimal.valueOf(2500.00))
                .withRate(BigDecimal.valueOf(5.5))
                .withPsk(BigDecimal.valueOf(6.0))
                .withIsInsuranceEnabled(true)
                .withIsSalaryClient(true)
                .build();

        statement = Statement.builder()
                .withStatementId(UUID.randomUUID())
                .withClientID(client)
                .withCreditID(credit)
                .withStatus(ApplicationStatusType.PREPARE_DOCUMENTS)
                .withCreationDate(LocalDateTime.now())
                .withAppliedOffer(LoanOfferDto.builder()
                        .withStatementId(UUID.randomUUID())
                        .withRequestedAmount(BigDecimal.valueOf(25000.00))
                        .withTotalAmount(BigDecimal.valueOf(30000.00))
                        .withTerm(12)
                        .withMonthlyPayment(BigDecimal.valueOf(2500.00))
                        .withRate(BigDecimal.valueOf(5.5))
                        .withIsInsuranceEnabled(true)
                        .withIsSalaryClient(true)
                        .build())
                .withSignDate(LocalDateTime.now().plusDays(5))
                .withStatementStatusHistory(Arrays.asList(
                        StatementStatusHistoryDto.builder().build()
                ))
                .build();

        finishRegistrationRequestDto = FinishRegistrationRequestDto.builder()
                .withGender(GenderType.MALE)
                .withMaritalStatus(MaritalStatusType.SINGLE)
                .withDependentAmount(0)
                .withPassportIssueDate(LocalDate.of(2015, 6, 20))
                .withPassportIssueBranch("Moscow")
                .withEmployment(EmploymentDto.builder()
                        .withEmploymentStatus(EmploymentStatusType.STUDENT)
                        .withPosition(PositionType.OTHER)
                        .withSalary(BigDecimal.valueOf(50000))
                        .build())
                .withAccountNumber("1234567890")
                .build();
    }

    @Test
    void buildScoringData_ShouldBuildScoringDataDto() {
        ScoringDataDto scoringData = loanStatementService.buildScoringData(client, loanOffer, finishRegistrationRequestDto);

        assertNotNull(scoringData);
        assertEquals(client.getFirstName(), scoringData.firstName());
        assertEquals(client.getLastName(), scoringData.lastName());
        assertEquals(loanOffer.getRequestedAmount(), scoringData.amount());
        assertEquals(loanOffer.getTerm(), scoringData.term());
        assertEquals(finishRegistrationRequestDto.passportIssueDate(), scoringData.passportIssueDate());
    }

    @Test
    void getStatementById_ShouldReturnStatement() {
        when(statementRepository.findById(statement.getStatementId())).thenReturn(java.util.Optional.of(statement));

        Statement foundStatement = loanStatementService.getStatementById(statement.getStatementId());

        assertNotNull(foundStatement);
        assertEquals(statement.getStatementId(), foundStatement.getStatementId());
    }

    @Test
    void saveStatement_ShouldSaveAndReturnStatement() {
        when(statementRepository.save(any(Statement.class))).thenReturn(statement);

        Statement savedStatement = loanStatementService.saveStatement(client);

        assertNotNull(savedStatement);
        assertEquals(client, savedStatement.getClientID());
        assertEquals(ApplicationStatusType.PREPARE_DOCUMENTS, savedStatement.getStatus());
        assertNotNull(savedStatement.getCreationDate());
        assertFalse(savedStatement.getStatementStatusHistory().isEmpty());

        verify(statementRepository, times(1)).save(any(Statement.class));
    }
}
