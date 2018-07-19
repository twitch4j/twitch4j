package me.philippheuer.util.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class VideoIdDeserializer extends JsonDeserializer<Long> {
	@Override
	public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		if (p.getValueAsString().startsWith("v")) {
			return Long.parseLong(p.getValueAsString().substring(1));
		}
		else return ctxt.readValue(p, Long.class);
	}
}
