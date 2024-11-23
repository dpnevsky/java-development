package calculator.service.impl;

import calculator.dto.LoanOfferDto;
import calculator.dto.LoanStatementRequestDto;
import calculator.service.util.ServiceForCalculate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LoanOfferServiceImplTest {

    @Value("${loan.base-rate}")
    private BigDecimal baseRate;

    @Value("${loan.salary-client-rate-discount}")
    private BigDecimal salaryClientDiscount;

    @Value("${loan.insurance-rate-discount}")
    private BigDecimal insuranceRateDiscount;

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
        ReflectionTestUtils.setField(loanOfferService, "baseRate", baseRate);
        ReflectionTestUtils.setField(loanOfferService, "salaryClientDiscount", salaryClientDiscount);
        ReflectionTestUtils.setField(loanOfferService, "insuranceRateDiscount", insuranceRateDiscount);
    }

    @Test
    void testConfigValues() {

        assertNotNull(baseRate, "Base rate must not be null");
        assertNotNull(salaryClientDiscount, "Salary client discount must not be null");
        assertNotNull(insuranceRateDiscount, "Insurance rate discount must not be null");
    }

    @Test
    void generateLoanOffersValidRequestSuccess() {
        offers = loanOfferService.generateLoanOffers(request);

        assertNotNull(offers);
        assertEquals(4, offers.size());
    }

    @Test
    void generateLoanOffersCheckRateCalculation() {
        offers = loanOfferService.generateLoanOffers(request);

        assertTrue(offers.stream().allMatch(offer -> offer.rate().compareTo(BigDecimal.ZERO) > 0));
    }

    @Test
    void generateLoanOffersSortedByRate() {
        offers = loanOfferService.generateLoanOffers(request);

        assertEquals(4, offers.size());
        for (int i = 1; i < offers.size(); i++) {
            assertTrue(offers.get(i - 1).rate().compareTo(offers.get(i).rate()) >= 0);
        }
    }
}
