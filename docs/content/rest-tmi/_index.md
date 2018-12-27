+++
title="API - TMI"
weight = 10
+++

# Twitch API - Messaging Interface

The Twitch Chat System has a few unofficial endpoints which get exposed from `https://tmi.twitch.tv`.

## As Module: Twitch4J

To use TMI you need to enable the TMI API when building the Twitch4J Instance, as shown below:

```java
TwitchClient twitchClient = TwitchClientBuilder.builder()
    ...
    .withEnableTMI(true)
    ...
    .build();
```

## Standalone

Initialize the Helix API as Standalone Module:

```java
TwitchHelix client = TwitchMessagingInterfaceBuilder.builder()
    .withClientId("clientId")
    .withClientSecret("clientSecret")
    .build();
```

## API Methods

- [TMI -> Get Viewers](./get-viewers)
