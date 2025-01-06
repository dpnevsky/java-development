package dossier.service;

import core.dto.EmailMessage;

public interface DossierKafkaConsumerService {
    void consumeMessage(EmailMessage message);
}
