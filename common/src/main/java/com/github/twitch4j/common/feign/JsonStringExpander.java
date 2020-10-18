package com.github.twitch4j.common.feign;

import feign.Param;
import org.apache.commons.lang3.StringUtils;

/**
 * Escapes a string so that it can be safely inserted in an existing JSON string that is within the {@link feign.Body}
 */
public class JsonStringExpander implements Param.Expander {

    @Override
    public String expand(Object value) {
        return value == null ? "" : StringUtils.replaceEach(value.toString(), new String[] {
            "\"",
            "\\",
            "\t",
            "\r",
            "\n",
            "\b",
            "\f"
        }, new String[] {
            "\\\"",
            "\\\\",
            "\\t",
            "\\r",
            "\\n",
            "\\b",
            "\\f"
        });
    }

}
