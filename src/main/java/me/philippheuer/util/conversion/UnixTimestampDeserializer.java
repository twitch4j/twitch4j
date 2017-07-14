package me.philippheuer.util.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Calendar;

public class UnixTimestampDeserializer extends JsonDeserializer<Calendar> {

	@Override
	public Calendar deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
		String dateAsString = jsonparser.getText();
		Calendar calendar = Calendar.getInstance();

		try {
			calendar.setTimeInMillis(Long.valueOf(dateAsString) * 1000L);
		} catch (NumberFormatException e) {
			calendar.set(1970, 1, 1);
		}

		return calendar;
	}
}
