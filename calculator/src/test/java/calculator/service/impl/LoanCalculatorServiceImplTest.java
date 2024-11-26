package calculator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import calculator.dto.CreditDto;
import calculator.dto.EmploymentDto;
import calculator.dto.PaymentScheduleElementDto;
import calculator.dto.ScoringDataDto;
import calculator.exception.LoanCheckException;
import calculator.service.util.ServiceForCalculate;
import calculator.service.util.loancheck.LoanApplicationProcessor;
import calculator.type.EmploymentStatusType;
import calculator.type.GenderType;
import calculator.type.MaritalStatusType;
import calculator.type.PositionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class LoanCalculatorServiceImplTest {

    @Value("${loan.base-rate}")
    private BigDecimal baseRate;

    @Mock
    private ServiceForCalculate serviceForCalculate;

    @Mock
    private LoanApplicationProcessor loanApplicationProcessor;

    @Autowired
    private LoanCalculatorServiceImpl loanCalculatorService;

    private ScoringDataDto scoringDataDto;
    private CreditDto creditDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scoringDataDto = ScoringDataDto.builder()
                .withEmployment(EmploymentDto.builder()
                        .withEmploymentStatus(EmploymentStatusType.EMPLOYED)
                        .withSalary(BigDecimal.valueOf(30000))
                        .withEmployerINN("12345678912")
                        .withPosition(PositionType.JUNIOR_MANAGER)
                        .withWorkExperienceTotal(19)
                        .withWorkExperienceCurrent(6)
                        .build())
                .withTerm(12)
                .withMaritalStatus(MaritalStatusType.DIVORCED)
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportIssueBranch("Moscow")
                .withGender(GenderType.MALE)
                .withAmount(BigDecimal.valueOf(20000))
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .withIsInsuranceEnabled(false)
                .withIsSalaryClient(false)
                .build();
    }

    @Test
    void testConfigValues() {
        assertNotNull(baseRate, "Base rate must not be null");
    }

    @Test
    void calculateLoanValidInputSuccessfulCalculation() throws LoanCheckException {
        when(loanApplicationProcessor.processLoanApplication(scoringDataDto, baseRate)).thenReturn(BigDecimal.valueOf(12));

        creditDto = loanCalculatorService.calculateLoan(scoringDataDto);

        assertNotNull(creditDto);
        assertEquals(BigDecimal.valueOf(1786), creditDto.monthlyPayment());
        assertEquals(BigDecimal.valueOf(13.0), creditDto.psk().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(12, creditDto.term());
    }

    @Test
    void calculateLoanExpectedLastRemainingDebtZero() {
        creditDto = loanCalculatorService.calculateLoan(scoringDataDto);

        assertEquals(creditDto.paymentSchedule().get(creditDto.term() - 1).remainingDebt(), BigDecimal.ZERO);
    }

    @Test
    void generatePaymentScheduleValidInputReturnsSchedule() {
        creditDto = loanCalculatorService.calculateLoan(scoringDataDto);

        Integer term = 12;
        List<PaymentScheduleElementDto> schedule = loanCalculatorService.calculateLoan(scoringDataDto).paymentSchedule();

        assertNotNull(schedule);
        assertEquals(term, schedule.size());
        assertTrue(schedule.stream().allMatch(element -> element.totalPayment().compareTo(BigDecimal.ZERO) > 0));
    }
}
