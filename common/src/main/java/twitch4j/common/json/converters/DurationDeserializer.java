package twitch4j.common.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Pattern;

public class DurationDeserializer extends JsonDeserializer<Duration> {
	@Override
	public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		if (p.getValueAsString().matches("([0-9]+h)?([0-9]+m)?[0-9]+s")) {
			Duration.parse("PT" + p.getValueAsString());
		} else if (Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)D)?" +
						"(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?",
				Pattern.CASE_INSENSITIVE).matcher(p.getValueAsString()).matches()) {
			Duration.parse(p.getValueAsString());
		}

		return Duration.ZERO;
	}
}
