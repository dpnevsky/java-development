package deal.service.impl;

import deal.persistence.model.Credit;
import deal.persistence.repository.CreditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CreditServiceImplTest {

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditServiceImpl creditService;

    private Credit credit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        credit = Credit.builder()
                .withAmount(BigDecimal.valueOf(25000))
                .withTerm(12)
                .withMonthlyPayment(BigDecimal.valueOf(2500))
                .withIsInsuranceEnabled(true)
                .withIsSalaryClient(true)
                .build();
    }

    @Test
    void saveCredit_ShouldSaveCreditSuccessfully() {
        when(creditRepository.save(credit)).thenReturn(credit);

        Credit savedCredit = creditService.saveCredit(credit);

        assertNotNull(savedCredit);
        assertEquals(BigDecimal.valueOf(25000), savedCredit.getAmount());
        assertEquals(12, savedCredit.getTerm());
        assertEquals(BigDecimal.valueOf(2500), savedCredit.getMonthlyPayment());

        verify(creditRepository, times(1)).save(credit);
    }

    @Test
    void saveCredit_ShouldReturnNullIfRepositoryReturnsNull() {
        when(creditRepository.save(credit)).thenReturn(null);

        Credit savedCredit = creditService.saveCredit(credit);

        assertNull(savedCredit);

        verify(creditRepository, times(1)).save(credit);
    }
}
