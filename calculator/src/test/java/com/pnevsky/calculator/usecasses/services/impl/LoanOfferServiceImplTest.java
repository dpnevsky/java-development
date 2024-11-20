package com.pnevsky.calculator.usecasses.services.impl;

import com.pnevsky.calculator.usecasses.dto.LoanOfferDto;
import com.pnevsky.calculator.usecasses.dto.LoanStatementRequestDto;
import com.pnevsky.calculator.usecasses.services.util.ServiceForCalculate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LoanOfferServiceImplTest {

    @Mock
    private ServiceForCalculate serviceForCalculate;

    @InjectMocks
    private LoanOfferServiceImpl loanOfferService;
    private LoanStatementRequestDto request;
    private List<LoanOfferDto> offers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = LoanStatementRequestDto.builder()
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

        ReflectionTestUtils.setField(loanOfferService, "baseRate", BigDecimal.valueOf(12));
        ReflectionTestUtils.setField(loanOfferService, "salaryClientDiscount", BigDecimal.valueOf(1));
        ReflectionTestUtils.setField(loanOfferService, "insuranceRateDiscount", BigDecimal.valueOf(2));
        offers = loanOfferService.generateLoanOffers(request);
    }

    @Test
    void generateLoanOffersValidRequestSuccess() {

        assertNotNull(offers);
        assertEquals(4, offers.size());
    }

    @Test
    void generateLoanOffersCheckRateCalculation() {

        assertTrue(offers.stream().allMatch(offer -> offer.rate().compareTo(BigDecimal.ZERO) > 0));
    }

    @Test
    void generateLoanOffersSortedByRate() {

        assertEquals(4, offers.size());
        for (int i = 1; i < offers.size(); i++) {
            assertTrue(offers.get(i - 1).rate().compareTo(offers.get(i).rate()) >= 0);
        }
    }
}
