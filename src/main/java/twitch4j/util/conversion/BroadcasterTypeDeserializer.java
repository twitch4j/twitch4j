package twitch4j.util.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import twitch4j.enums.BroadcasterType;

public class BroadcasterTypeDeserializer extends JsonDeserializer<BroadcasterType> {
	@Override
	public BroadcasterType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return BroadcasterType.of(p.getValueAsString());
	}
}
