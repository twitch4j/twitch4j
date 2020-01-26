+++
title="Client-Builder"
weight = 5
+++

# Twitch4J Client Builder

## All Features
Initialize the Twitch4J Client and specify which modules you want to use:

```java
TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEnableHelix(true)
            .build();
```

## Overwrite the EventManager for Cross-Library Usage

You can overwrite the native EventManager with a commonly used one to share events between Twitch4J and other 3rd party libraries.

```java
TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEventManager(eventManager)
            .build();
```

## Logging

Please check out [Logging](./logging) on how to set up logging.
