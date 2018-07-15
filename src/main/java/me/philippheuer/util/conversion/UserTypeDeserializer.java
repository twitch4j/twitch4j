package me.philippheuer.util.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import me.philippheuer.twitch4j.enums.UserType;

public class UserTypeDeserializer extends JsonDeserializer<UserType> {
	@Override
	public UserType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return UserType.of(p.getValueAsString());
	}
}
