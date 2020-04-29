package com.github.twitch4j.helix;

import com.github.twitch4j.common.util.TypeConvert;
import feign.Param;

public class ObjectToJsonExpander implements Param.Expander{

	@Override
	public String expand(Object value) {
		return TypeConvert.objectToJson(value);
	}

}
