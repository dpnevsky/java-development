package com.pnevsky.calculator.usecasses.services.impl;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.CreditDto;
import com.pnevsky.calculator.usecasses.dto.EmploymentDto;
import com.pnevsky.calculator.usecasses.dto.PaymentScheduleElementDto;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import com.pnevsky.calculator.usecasses.services.util.ServiceForCalculate;
import com.pnevsky.calculator.usecasses.services.util.loancheck.LoanApplicationProcessor;
import com.pnevsky.calculator.usecasses.types.EmploymentStatusType;
import com.pnevsky.calculator.usecasses.types.GenderType;
import com.pnevsky.calculator.usecasses.types.MaritalStatusType;
import com.pnevsky.calculator.usecasses.types.PositionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoanCalculatorServiceImplTest {

    @Mock
    private ServiceForCalculate serviceForCalculate;

    @Mock
    private LoanApplicationProcessor loanApplicationProcessor;

    @InjectMocks
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
                        .withPosition(PositionType.MANAGER)
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
        ReflectionTestUtils.setField(loanCalculatorService, "baseRate", BigDecimal.valueOf(12));
        creditDto = loanCalculatorService.calculateLoan(scoringDataDto);
    }

    @Test
    void calculateLoanValidInputSuccessfulCalculation() throws LoanCheckException {

        assertNotNull(creditDto);
        assertEquals(BigDecimal.valueOf(1786), creditDto.monthlyPayment());
        assertEquals(BigDecimal.valueOf(13.0), creditDto.psk().setScale(1, RoundingMode.HALF_EVEN));
        assertEquals(12, creditDto.term());
    }

    @Test
    void calculateLoanExpectedLastRemainingDebtZero() {

        assertEquals(creditDto.paymentSchedule().get(creditDto.term() - 1).remainingDebt(), BigDecimal.ZERO);
    }

    @Test
    void generatePaymentScheduleValidInputReturnsSchedule() {

        Integer term = 12;

        List<PaymentScheduleElementDto> schedule = loanCalculatorService
                .calculateLoan(scoringDataDto).paymentSchedule();

        assertNotNull(schedule);
        assertEquals(term, schedule.size());
        assertTrue(schedule.stream().allMatch(element -> element.totalPayment().compareTo(BigDecimal.ZERO) > 0));
    }
}
