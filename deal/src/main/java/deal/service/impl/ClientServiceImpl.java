package deal.service.impl;

import deal.mapper.ClientMapper;
import deal.persistence.model.Client;
import deal.persistence.repository.ClientRepository;
import deal.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import core.dto.LoanStatementRequestDto;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public Client saveClient(LoanStatementRequestDto loanStatementRequestDto) {
        Client client = clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto);
        return clientRepository.save(client);
    }

    @Override
    public String getEmailByStatementId(UUID statementId) {
        return clientRepository.findEmailByStatementId(statementId);
    }
}
