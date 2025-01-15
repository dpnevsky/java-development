package deal.client.impl;

import core.dto.EmailMessage;
import core.type.KafkaTopicNameType;
import deal.client.DealKafkaProducerClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DealKafkaProducerClientServiceImpl implements DealKafkaProducerClientService {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void sendDocuments(KafkaTopicNameType topic, EmailMessage message) {
        kafkaTemplate.send(topic.getTopicName(), message);
    }
}
