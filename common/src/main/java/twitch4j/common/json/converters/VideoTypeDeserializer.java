package twitch4j.common.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import twitch4j.common.enums.VideoType;

import java.io.IOException;

public class VideoTypeDeserializer extends JsonDeserializer<VideoType> {

	@Override
	public VideoType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return VideoType.of(p.getValueAsString());
	}
}
