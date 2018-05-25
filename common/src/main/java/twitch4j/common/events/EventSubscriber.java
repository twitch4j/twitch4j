package twitch4j.common.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a method that handles events.
 *
 * @author Austin [https://github.com/austinv11]
 * @version %I%, %G%
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscriber {
	// TODO Priority Filter
//	Priority value() default Priority.LOW;
//
//	enum Priority {
//		LOW,
//		MEDIUM,
//		HIGH
//	}
}
