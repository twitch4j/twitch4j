package io.twitch4j.annotation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation processing for Events. Using Immutables. Works similar like {@link ModelProcessor}
 *
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @see <a href="https://immutables.github.io/json.html">https://immutables.github.io/json.html</a>
 * @since 1.0
 */
@Value.Style(
        get = {"get*", "has*", "is*", "*"},
        typeAbstract = "*",
        typeImmutable = "*Impl",
        builder = "new",
        beanFriendlyModifiables = true,
        visibility = Value.Style.ImplementationVisibility.PRIVATE,
        builderVisibility = Value.Style.BuilderVisibility.PUBLIC,
        defaultAsDefault = true,
        forceJacksonIgnoreFields = true
)
@JsonSerialize
@Retention(RetentionPolicy.SOURCE)
public @interface EventProcessor {
}
