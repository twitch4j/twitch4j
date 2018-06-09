package twitch4j.common.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The Unofficial Annotation marks unofficial api endpoints.
 * <p>
 * The support of unofficial endpoints can break at any point.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Unofficial {
	String source() default "https://dev.twitch.tv/";
}
