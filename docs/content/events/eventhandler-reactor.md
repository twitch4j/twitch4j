+++
title="EventHandler - Reactor"
weight = 202
+++

# EventHandler - Reactor

## Description

The ReactorEventHandler will process all events asynchronous in a threadpool with at least 4 threads.

Check out the following class if you want to customize the parameters used when building a ReactorEventHandler instance:

- [ReactorEventHandler.java](https://github.com/PhilippHeuer/events4j/blob/master/handler-reactor/src/main/java/com/github/philippheuer/events4j/reactor/ReactorEventHandler.java)

## Dependencies

Gradle:

`api group: 'com.github.philippheuer.events4j', name: 'events4j-handler-reactor', version: '0.9.2'`

Maven:

```xml
<dependency>
    <groupId>com.github.philippheuer.events4j</groupId>
    <artifactId>events4j-handler-reactor</artifactId>
    <version>0.9.2</version>
</dependency>
```

## Set as default in Twitch4J and for eventManager.onEvent

```java
TwitchClient twitchClient = TwitchClientBuilder.builder()
    .withDefaultEventHandler(ReactorEventHandler.class)
    .build();
```
