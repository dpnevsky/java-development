package deal.service.impl;

import core.dto.LoanStatementRequestDto;
import deal.mapper.ClientMapper;
import deal.persistence.model.Client;
import deal.persistence.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private LoanStatementRequestDto loanStatementRequestDto;
    private Client client;

    @BeforeEach
    void setUp() {
        loanStatementRequestDto = LoanStatementRequestDto.builder()
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
                .build();
    }

    @Test
    void saveClient_ShouldSaveAndReturnClient() {
        when(clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);

        Client savedClient = clientService.saveClient(loanStatementRequestDto);

        assertNotNull(savedClient);
        assertEquals("Ivan", savedClient.getFirstName());
        assertEquals("Ivanov", savedClient.getLastName());

        verify(clientMapper).loanStatementRequestDtoToClient(loanStatementRequestDto);
        verify(clientRepository).save(client);
    }
}
