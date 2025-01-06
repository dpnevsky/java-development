package core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.dto.EmailMessage;
import org.apache.kafka.common.serialization.Deserializer;

public class EmailMessageDeserializer implements Deserializer<EmailMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public EmailMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, EmailMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing message", e);
        }
    }
}
