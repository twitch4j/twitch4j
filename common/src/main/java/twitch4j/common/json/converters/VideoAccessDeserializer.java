package twitch4j.common.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import twitch4j.common.enums.VideoAccess;

import java.io.IOException;

public class VideoAccessDeserializer extends JsonDeserializer<VideoAccess> {

	@Override
	public VideoAccess deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return VideoAccess.of(p.getValueAsString());
	}
}
