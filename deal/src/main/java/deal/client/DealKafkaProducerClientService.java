package deal.client;

import core.dto.EmailMessage;
import core.type.KafkaTopicNameType;

public interface DealKafkaProducerClientService {
    void sendDocuments(KafkaTopicNameType topic, EmailMessage message);
}
