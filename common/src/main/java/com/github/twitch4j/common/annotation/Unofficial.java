package com.github.twitch4j.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotates a method or features that uses unofficial api endpoints, those can break at any point in time. Use at your own risk.
 * <p>
 * All aspects of this functionality including query parameters and response structure are subject to change without notice.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Unofficial {

}
