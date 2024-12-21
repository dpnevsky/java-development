package statement.controller;

import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import statement.client.DealServiceRestClient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StatementApiControllerTest {

    @Mock
    private DealServiceRestClient dealServiceRestClient;

    @InjectMocks
    private StatementApiController statementApiController;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        this.validator = factoryBean;
    }

    @Test
    void whenValidRequest_thenReturnsLoanOffers() {
        LoanStatementRequestDto validRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .build();

        ResponseEntity<List<LoanOfferDto>> response = statementApiController.getLoanOffers(validRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void whenInvalidAmount_thenThrowsValidationException() {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(10000))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("amount") &&
                        v.getMessage().equals("Amount must be greater than or equal to 20000")));
    }

    @Test
    void whenInvalidEmail_thenThrowsValidationException() {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("invalid-email")
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email") &&
                        v.getMessage().equals("Email must be a valid address")));
    }

    @Test
    void whenUnderageBorrower_thenThrowsValidationException() {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(2020, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("ageValid") &&
                        v.getMessage().equals("Borrower must be at least 18 years old")));
    }

    @Test
    void whenInvalidPassportSeries_thenThrowsValidationException() {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportSeries("12")
                .withPassportNumber("654321")
                .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("passportSeries") &&
                        v.getMessage().equals("Passport series must be 4 digits")));
    }

    @Test
    void whenInvalidTerm_thenThrowsValidationException() {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000))
                .withTerm(3)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("term") &&
                        v.getMessage().equals("Term must be at least 6 months")));
    }

    @Test
    void whenInvalidFirstName_thenThrowsValidationException() {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000))
                .withTerm(12)
                .withFirstName("I")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName") &&
                            v.getMessage().equals("First name must be between 2 and 30 Latin letters")));
    }

    @Test
    void whenInvalidLastName_thenThrowsValidationException() {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("I")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("654321")
                .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName") &&
                        v.getMessage().equals("Last name must be between 2 and 30 Latin letters")));
    }

    @Test
    void whenInvalidMiddleName_thenThrowsValidationException() {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                    .withAmount(BigDecimal.valueOf(20000))
                    .withTerm(12)
                    .withFirstName("Ivan")
                    .withLastName("Ivanov")
                    .withMiddleName("1")
                    .withEmail("ivan.ivanov@yandex.ru")
                    .withBirthdate(LocalDate.of(2000, 1, 1))
                    .withPassportSeries("1234")
                    .withPassportNumber("654321")
                    .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("middleName") &&
                        v.getMessage().equals("Middle name must be between 2 and 30 Latin letters")));
    }

    @Test
    void whenInvalidPassportNumber_thenThrowsValidationException() {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder()
                .withAmount(BigDecimal.valueOf(20000))
                .withTerm(12)
                .withFirstName("Ivan")
                .withLastName("Ivanov")
                .withMiddleName("Dmitrievich")
                .withEmail("ivan.ivanov@yandex.ru")
                .withBirthdate(LocalDate.of(2000, 1, 1))
                .withPassportSeries("1234")
                .withPassportNumber("12345")
                .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("passportNumber") &&
                        v.getMessage().equals("Passport number must be 6 digits")));
    }

    @Test
    void selectLoanOfferController_shouldInvokeService() {
        DealServiceRestClient mockDealServiceRestClient = mock(DealServiceRestClient.class);
        StatementApiController controller = new StatementApiController(mockDealServiceRestClient);

        LoanOfferDto offer = new LoanOfferDto();
        controller.selectLoanOffer(offer);

        verify(mockDealServiceRestClient, times(1)).selectLoanOffer(offer);
    }
}
