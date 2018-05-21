package twitch4j.common.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
