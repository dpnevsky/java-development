package core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.dto.EmailMessage;
import org.apache.kafka.common.serialization.Serializer;

public class EmailMessageSerializer implements Serializer<EmailMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, EmailMessage emailMessage) {
        try {
            return objectMapper.writeValueAsBytes(emailMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize EmailMessage", e);
        }
    }
}
