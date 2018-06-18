package twitch4j.common.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import twitch4j.common.enums.UserType;

import java.io.IOException;

public class UserTypeDeserializer extends JsonDeserializer<UserType> {
	@Override
	public UserType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return UserType.of(p.getValueAsString());
	}
}
