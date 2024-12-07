package deal.service.impl;

import deal.persistence.model.Credit;
import deal.persistence.repository.CreditRepository;
import deal.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    @Override
    public Credit saveCredit(Credit credit) {
        return creditRepository.save(credit);
    }
}
