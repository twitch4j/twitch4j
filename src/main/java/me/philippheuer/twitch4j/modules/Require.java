package me.philippheuer.twitch4j.modules;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Require {
	String value();
}
