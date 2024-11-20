package com.pnevsky.calculator.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnevsky.calculator.usecasses.dto.CreditDto;
import com.pnevsky.calculator.usecasses.dto.EmploymentDto;
import com.pnevsky.calculator.usecasses.dto.LoanOfferDto;
import com.pnevsky.calculator.usecasses.dto.LoanStatementRequestDto;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import com.pnevsky.calculator.usecasses.services.LoanCalculatorService;
import com.pnevsky.calculator.usecasses.services.LoanOfferService;
import com.pnevsky.calculator.usecasses.types.EmploymentStatusType;
import com.pnevsky.calculator.usecasses.types.GenderType;
import com.pnevsky.calculator.usecasses.types.MaritalStatusType;
import com.pnevsky.calculator.usecasses.types.PositionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorApiController.class)
class CalculatorApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private LoanCalculatorService loanCalculatorService;

    @MockBean
    private LoanOfferService loanOfferService;

    private LoanStatementRequestDto validLoanRequest;
    private ScoringDataDto validScoringRequest;
    private LoanOfferDto validLoanOffer;
    private CreditDto validCreditResponse;

    @BeforeEach
    void setUp() {
        validLoanRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(1990, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .build();

        validLoanOffer = LoanOfferDto.builder()
                .withStatementId(UUID.randomUUID())
                .withRequestedAmount(BigDecimal.valueOf(20000))
                .withTotalAmount(BigDecimal.valueOf(21000))
                .withTerm(12)
                .withMonthlyPayment(BigDecimal.valueOf(1750))
                .withRate(BigDecimal.valueOf(5.5))
                .withIsInsuranceEnabled(false)
                .withIsSalaryClient(true)
                .build();

        validScoringRequest = ScoringDataDto.builder()
                .withAmount(BigDecimal.valueOf(30000))
                .withTerm(24)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withGender(GenderType.MALE)
                .withBirthdate(LocalDate.of(1990, 1, 1))
                .withMaritalStatus(MaritalStatusType.SINGLE)
                .withDependentAmount(1)
                .withPassportIssueBranch("Moscow")
                .withEmployment(
                        EmploymentDto.builder()
                                .withEmploymentStatus(EmploymentStatusType.EMPLOYED)
                                .withSalary(BigDecimal.valueOf(5000))
                                .withPosition(PositionType.MANAGER)
                                .withWorkExperienceTotal(25)
                                .withWorkExperienceCurrent(4)
                                .build()
                )
                .withAccountNumber("1234567890")
                .withIsInsuranceEnabled(true)
                .withIsSalaryClient(true)
                .build();

        validCreditResponse = CreditDto.builder()
                .withMonthlyPayment(BigDecimal.valueOf(1500))
                .withAmount(BigDecimal.valueOf(36000))
                .withPsk(BigDecimal.valueOf(6.5))
                .build();
    }

    @Test
    void generateLoanOffersWithValidRequestShouldReturnOffers() throws Exception {
        when(loanOfferService.generateLoanOffers(validLoanRequest)).thenReturn(Collections.singletonList(validLoanOffer));

        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoanRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].requestedAmount").value("20000"))
                .andExpect(jsonPath("$[0].rate").value("5.5"));
    }

    @Test
    void generateLoanOffersWithMissingFieldsShouldReturnBadRequest() throws Exception {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder().withAmount(null).build();

        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateLoanOffersWithBadTermShouldReturnBadRequest() throws Exception {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder().withTerm(1).build();

        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Term must be at least 6 months")));
    }

    @Test
    void generateLoanOffersWithBadAmountShouldReturnBadRequest() throws Exception {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(-1))
                .withTerm(12)
                .build();

        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Amount must be greater than or equal to 20000")));
    }

    @Test
    void calculateLoanWithValidRequestShouldReturnCreditDetails() throws Exception {
        when(loanCalculatorService.calculateLoan(validScoringRequest)).thenReturn(validCreditResponse);

        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validScoringRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyPayment").value("1500"))
                .andExpect(jsonPath("$.amount").value("36000"))
                .andExpect(jsonPath("$.psk").value("6.5"));
    }

    @Test
    void calculateLoanWithNegativeAmountShouldReturnBadRequest() throws Exception {
        ScoringDataDto invalidRequest = ScoringDataDto.builder()
                .withAmount(BigDecimal.valueOf(-5000))
                .build();

        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Amount must be greater than or equal to 20000")));
    }

    @Test
    void calculateLoanWithInvalidEmploymentShouldReturnBadRequest() throws Exception {
        ScoringDataDto invalidRequest = ScoringDataDto.builder()
                .withEmployment(EmploymentDto.builder().withEmployerINN("12345678901").build())
                .build();

        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Employer INN must be a 10-digit number")));
    }
}
