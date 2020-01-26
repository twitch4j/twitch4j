+++
title="Logging"
weight = 20
+++

# Logging

You can use any logging framework of your choice with *Twitch4J*, but you have to add at least one so that logging works correctly. 

If you do not ad at least one, you will see the following output:

```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```

Here are a few examples on how to add a logging library of your choice:

## Library: Logback

*Gradle*
```groovy
dependencies {
    compile group: 'ch.qos.logback', name: 'logback-classic', version: '1.2.3'
}
```

*Maven*
```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>
```

And place a configuration file `logback.xml` into `src/main/resources`. This is the place you can use to configure logging:

logback.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration scan="true">
	<!-- Configure Twitch4J logger level -->
	<logger name="com.github.twitch4j" level="INFO"/>

	<!-- Appender Configuration [Console] -->
	<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%d{HH:mm:ss.SSS} [%thread] %highlight([%-5level]) %logger{25} - %msg%n</pattern>
		</encoder>
	</appender>

	<!-- Appender Configuration [File] -->
	<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
		</encoder>
		<file>client.log</file>
		<rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
			<fileNamePattern>client.%i.log</fileNamePattern>
		</rollingPolicy>
		<triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
			<MaxFileSize>10MB</MaxFileSize>
		</triggeringPolicy>
	</appender>

	<!-- Appender Output Configuration -->
	<root level="info">
		<appender-ref ref="CONSOLE"/>
	</root>
</configuration>
```

This sample only logs to the console, you can change the appender-ref from `CONSOLE` to `FILE` to use file-based logging with rotation - refer to the logback documentation for further customization options.
