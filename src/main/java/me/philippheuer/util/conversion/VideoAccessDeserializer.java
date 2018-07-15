package me.philippheuer.util.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import me.philippheuer.twitch4j.enums.VideoAccess;

public class VideoAccessDeserializer extends JsonDeserializer<VideoAccess> {

	@Override
	public VideoAccess deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return VideoAccess.of(p.getValueAsString());
	}
}
