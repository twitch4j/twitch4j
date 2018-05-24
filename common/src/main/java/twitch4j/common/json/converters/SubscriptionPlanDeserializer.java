package twitch4j.common.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import twitch4j.common.enums.SubscriptionPlan;

import java.io.IOException;

public class SubscriptionPlanDeserializer extends JsonDeserializer<SubscriptionPlan> {
	@Override
	public SubscriptionPlan deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return SubscriptionPlan.fromString(p.getValueAsString());
	}
}
