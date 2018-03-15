package io.twitch4j.annotation;

import org.immutables.value.Value;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * BuilderProcessor Annotation processing. Using Immutables
 *
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @see <a href="https://immutables.github.io/immutable.html#builder">https://immutables.github.io/immutable.html#builder</a>
 * @since 1.0
 */
@Value.Style(
        get = {"get*", "has*", "is*", "*"},
        typeAbstract = "I*",
        typeImmutable = "*",
        typeBuilder = "*Builder",
        visibility = Value.Style.ImplementationVisibility.PRIVATE,
        builderVisibility = Value.Style.BuilderVisibility.PUBLIC,
        defaultAsDefault = true,
        defaults = @Value.Immutable(copy = false)
)
@Retention(RetentionPolicy.SOURCE)
public @interface BuilderProcessor {
}
