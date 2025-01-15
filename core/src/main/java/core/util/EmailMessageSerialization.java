package core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.dto.EmailMessage;
import core.exception.EmailMessageSerializationException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailMessageSerialization implements Serializer<EmailMessage>, Deserializer<EmailMessage> {

    private final ObjectMapper objectMapper;

    @Override
    public byte[] serialize(String topic, EmailMessage emailMessage) {
        try {
                return objectMapper.writeValueAsBytes(emailMessage);
        } catch (Exception e) {
                throw new EmailMessageSerializationException("Failed to serialize EmailMessage", e);
        }
    }

    @Override
    public EmailMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, EmailMessage.class);
        } catch (Exception e) {
            throw new EmailMessageSerializationException("Error deserializing EmailMessage", e);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }
}
