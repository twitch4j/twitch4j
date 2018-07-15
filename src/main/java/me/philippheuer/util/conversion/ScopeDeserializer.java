package me.philippheuer.util.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import me.philippheuer.twitch4j.enums.Scope;

public class ScopeDeserializer extends JsonDeserializer<Scope> {
	@Override
	public Scope deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return Scope.fromString(p.getValueAsString());
	}
}
