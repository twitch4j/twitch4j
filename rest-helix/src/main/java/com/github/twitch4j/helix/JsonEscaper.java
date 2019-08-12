package com.github.twitch4j.helix;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.core.util.BufferRecyclers;
import feign.Param;

public class JsonEscaper implements Param.Expander {

	private static final JsonStringEncoder JSON_ESCAPER = BufferRecyclers.getJsonStringEncoder();

	@Override
	public String expand(Object value) {
		if(value == null)
			return "";
		else
			return new String(JSON_ESCAPER.quoteAsString(value.toString()));
	}

}
