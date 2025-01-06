package deal.config;

import core.dto.EmailMessage;
import core.util.EmailMessageSerialization;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.context.annotation.Bean;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final EmailMessageSerialization emailMessageSerialization;

    @Bean
    public ProducerFactory<String, EmailMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, emailMessageSerialization.getClass());

        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), emailMessageSerialization);
    }

    @Bean
    public KafkaTemplate<String, EmailMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
