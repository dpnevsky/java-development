package deal.mapper;

import core.dto.LoanStatementRequestDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import deal.persistence.model.Client;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    @Test
    void loanStatementRequestDtoToClient_ShouldMapCorrectly() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
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

        Client client = clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto);

        assertNotNull(client);
        assertNotNull(client.getClientId());  // Проверяем, что сгенерирован ID
        assertEquals("Ivan", client.getFirstName());
        assertEquals("Ivanov", client.getLastName());
        assertEquals("Dmitrievich", client.getMiddleName());
        assertEquals(LocalDate.of(2000, 1, 1), client.getBirthDate());
        assertEquals("ivan.ivanov@yandex.ru", client.getEmail());
        assertNotNull(client.getPassport());
        assertEquals("654321", client.getPassport().getPassportNumber());
        assertEquals("1234", client.getPassport().getPassportSeries());
    }

    @Test
    void loanStatementRequestDtoToClient_ShouldHandleNullValues() {

        LoanStatementRequestDto dto = LoanStatementRequestDto.builder()
                .withFirstName(null)
                .withLastName(null)
                .withMiddleName(null)
                .withBirthdate(null)
                .withEmail(null)
                .withPassportSeries(null)
                .withPassportNumber(null)
                .build();

        Client client = clientMapper.loanStatementRequestDtoToClient(dto);

        assertNotNull(client);
        assertNull(client.getFirstName());
        assertNull(client.getLastName());
        assertNull(client.getMiddleName());
        assertNull(client.getBirthDate());
        assertNull(client.getEmail());
        assertNull(client.getPassport().getPassportId());
    }

    @Test
    void loanStatementRequestDtoToClient_ShouldGenerateUniqueClientId() {
        LoanStatementRequestDto dto = LoanStatementRequestDto.builder()
                .withFirstName("Alice")
                .withLastName("Smith")
                .withMiddleName("Test")
                .withBirthdate(LocalDate.of(1990, 5, 10))
                .withEmail("alice.smith@example.com")
                .withPassportSeries("9876")
                .withPassportNumber("XY9876543")
                .build();

        Client client1 = clientMapper.loanStatementRequestDtoToClient(dto);
        Client client2 = clientMapper.loanStatementRequestDtoToClient(dto);

        assertNotNull(client1.getClientId());
        assertNotNull(client2.getClientId());
        assertNotEquals(client1.getClientId(), client2.getClientId());
    }
}
