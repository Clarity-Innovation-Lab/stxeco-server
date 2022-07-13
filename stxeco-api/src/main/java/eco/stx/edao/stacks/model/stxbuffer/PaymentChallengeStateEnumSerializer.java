package eco.stx.edao.stacks.model.stxbuffer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class PaymentChallengeStateEnumSerializer extends JsonSerializer<PaymentChallengeStateEnum> {

	@Override
	public void serialize(PaymentChallengeStateEnum value, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		generator.writeStartObject();
		generator.writeFieldName("status");
		generator.writeNumber(value.getStatus());
		generator.writeFieldName("description");
		generator.writeString(value.getDescription());
		generator.writeEndObject();
	}
}