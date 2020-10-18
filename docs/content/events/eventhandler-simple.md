+++
title="EventHandler - Simple"
weight = 201
+++

# EventHandler - Simple

## Description

The SimpleEventHandler will process all events synchronous in the current thread.

## Dependencies

Gradle:

`api group: 'com.github.philippheuer.events4j', name: 'events4j-handler-simple', version: '0.9.2'`

Maven:

```xml
<dependency>
    <groupId>com.github.philippheuer.events4j</groupId>
    <artifactId>events4j-handler-simple</artifactId>
    <version>0.9.2</version>
</dependency>
```

## Set as default in Twitch4J and for eventManager.onEvent

```java
TwitchClient twitchClient = TwitchClientBuilder.builder()
    .withDefaultEventHandler(SimpleEventHandler.class)
    .build();
```
