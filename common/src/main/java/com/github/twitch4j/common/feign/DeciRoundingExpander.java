package com.github.twitch4j.common.feign;

import feign.Param;

import java.util.Objects;

/**
 * Converts floating-point numbers to strings with a single decimal place and appropriate rounding.
 */
public class DeciRoundingExpander implements Param.Expander {

    @Override
    public String expand(Object value) {
        long tenTimes;
        if (value instanceof Float) {
            tenTimes = Math.round((Float) value * 10);
        } else if (value instanceof Double) {
            tenTimes = Math.round((Double) value * 10);
        } else {
            return Objects.toString(value);
        }

        return tenTimes / 10 + "." + tenTimes % 10;
    }

}
