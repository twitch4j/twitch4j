package twitch4j.util.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import twitch4j.enums.SubscriptionPlan;

public class SubscriptionPlanDeserializer extends JsonDeserializer<SubscriptionPlan> {
	@Override
	public SubscriptionPlan deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return SubscriptionPlan.fromString(p.getValueAsString());
	}
}
