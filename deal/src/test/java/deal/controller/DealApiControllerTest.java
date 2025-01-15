package deal.controller;

import core.dto.CreditDto;
import core.dto.EmploymentDto;
import core.dto.FinishRegistrationRequestDto;
import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import core.dto.ScoringDataDto;
import core.dto.StatementStatusHistoryDto;
import core.type.ApplicationStatusType;
import core.type.EmploymentStatusType;
import core.type.GenderType;
import core.type.MaritalStatusType;
import core.type.PositionType;
import core.util.Passport;
import deal.client.CalculatorServiceRestClient;
import deal.client.DealKafkaProducerClientService;
import deal.mapper.CreditMapper;
import deal.persistence.model.Client;
import deal.persistence.model.Credit;
import deal.persistence.model.Statement;
import deal.service.ClientService;
import deal.service.CreditService;
import deal.service.LoanStatementService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealApiControllerTest {

    @Mock
    private LoanStatementService loanStatementService;

    @Mock
    DealKafkaProducerClientService dealKafkaProducerClientService;

    @Mock
    private CalculatorServiceRestClient calculatorServiceRestClient;

    @Mock
    private ClientService clientService;

    @Mock
    private CreditService creditService;

    @Mock
    private CreditMapper creditMapper;

    @InjectMocks
    private DealApiController dealApiController;

    private static LoanStatementRequestDto requestDto;
    private static Credit credit;
    private static Client client;
    private static Statement statement;
    private static LoanOfferDto loanOfferFirst;
    private static LoanOfferDto loanOfferSecond;
    private static List<LoanOfferDto> loanOffers;
    private static ScoringDataDto scoringDataDto;
    private static CreditDto creditDto;
    private static FinishRegistrationRequestDto finishRegistrationRequestDto;

    @BeforeAll
    static void setUp() {
        requestDto = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000.00))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .build();

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

        statement = Statement.builder()
                .withStatementId(UUID.randomUUID())
                .withClientID(client)
                .withCreditID(credit)
                .withStatus(ApplicationStatusType.APPROVED)
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

        loanOfferFirst = LoanOfferDto.builder()
                .withStatementId(UUID.randomUUID())
                .withRequestedAmount(BigDecimal.valueOf(25000.00))
                .withTotalAmount(BigDecimal.valueOf(30000.00))
                .withTerm(12)
                .withMonthlyPayment(BigDecimal.valueOf(2500.00))
                .withRate(BigDecimal.valueOf(5.5))
                .withIsInsuranceEnabled(true)
                .withIsSalaryClient(true)
                .build();

        loanOfferSecond = LoanOfferDto.builder()
                .withStatementId(UUID.randomUUID())
                .withRequestedAmount(BigDecimal.valueOf(18000.00))
                .withTotalAmount(BigDecimal.valueOf(30000.00))
                .withTerm(12)
                .withMonthlyPayment(BigDecimal.valueOf(2500.00))
                .withRate(BigDecimal.valueOf(4.5))
                .withIsInsuranceEnabled(true)
                .withIsSalaryClient(true)
                .build();

        loanOffers = List.of(loanOfferFirst, loanOfferSecond, loanOfferFirst, loanOfferSecond);

        scoringDataDto = ScoringDataDto.builder()
                .withAmount(BigDecimal.valueOf(25000.00))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withGender(GenderType.MALE)
                .withBirthdate(LocalDate.of(1990, 5, 10))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .withPassportIssueDate(LocalDate.of(2015, 6, 20))
                .withPassportIssueBranch("Moscow")
                .withMaritalStatus(MaritalStatusType.SINGLE)
                .withDependentAmount(0)
                .withEmployment(EmploymentDto.builder()
                        .withEmploymentStatus(EmploymentStatusType.STUDENT)
                        .withPosition(PositionType.OTHER)
                        .withSalary(BigDecimal.valueOf(50000))
                        .build())
                .withAccountNumber("1234567890")
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

        creditDto = CreditDto.builder()
                .withAmount(BigDecimal.valueOf(25000.00))
                .withTerm(12)
                .withMonthlyPayment(BigDecimal.valueOf(2500.00))
                .withRate(BigDecimal.valueOf(5.5))
                .withPsk(BigDecimal.valueOf(6.0))
                .withIsInsuranceEnabled(true)
                .withIsSalaryClient(true)
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
    void getLoanOffers_ShouldReturnLoanOffers() {
        when(clientService.saveClient(requestDto)).thenReturn(client);
        when(loanStatementService.saveStatement(client)).thenReturn(statement);
        when(calculatorServiceRestClient.getLoanOffers(requestDto)).thenReturn(loanOffers);

        ResponseEntity<List<LoanOfferDto>> response = dealApiController.getLoanOffers(requestDto);

        assertNotNull(response);
        assertEquals(4, response.getBody().size());
        assertEquals(BigDecimal.valueOf(25000.00), response.getBody().get(0).getRequestedAmount());
        assertEquals(BigDecimal.valueOf(5.5), response.getBody().get(0).getRate());
        assertEquals(BigDecimal.valueOf(30000.00), response.getBody().get(1).getTotalAmount());
        assertEquals(BigDecimal.valueOf(4.5), response.getBody().get(1).getRate());

        verify(clientService).saveClient(requestDto);
        verify(loanStatementService).saveStatement(client);
        verify(calculatorServiceRestClient).getLoanOffers(requestDto);
    }

    @Test
    void selectLoanOffer_ShouldUpdateStatementStatus() {
        when(loanStatementService.getStatementById(loanOfferFirst.getStatementId())).thenReturn(statement);

        dealApiController.selectLoanOffer(loanOfferFirst);

        verify(loanStatementService).getStatementById(loanOfferFirst.getStatementId());
        verify(loanStatementService).updateStatement(statement, loanOfferFirst, ApplicationStatusType.APPROVED);
        verify(loanStatementService).saveStatement(statement);
    }

    @Test
    void calculateLoan_ShouldUpdateStatementAndSaveCredit() {
        String statementId = statement.getStatementId().toString();
        statement.setAppliedOffer(loanOfferFirst);
        scoringDataDto = loanStatementService.buildScoringData(client, loanOfferFirst, finishRegistrationRequestDto);

        when(loanStatementService.getStatementById(UUID.fromString(statementId))).thenReturn(statement);
        doReturn(creditDto).when(calculatorServiceRestClient).calculateLoan(scoringDataDto);
        when(creditMapper.fromDtoToEntity(creditDto)).thenReturn(credit);

        dealApiController.calculateLoan(statementId, finishRegistrationRequestDto);

        verify(loanStatementService).getStatementById(UUID.fromString(statementId));
        verify(calculatorServiceRestClient).calculateLoan(scoringDataDto);
        verify(creditService).saveCredit(credit);
        verify(loanStatementService).updateStatement(statement, ApplicationStatusType.CC_APPROVED);
        verify(loanStatementService).saveStatement(statement);
    }
}
