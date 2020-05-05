+++
title="Twitch4J - Client Helper"
weight = 100
+++

# Twitch4J Client Helper

The Twitch4J Client Helper aims to bundle some common code used by library users to prevent everyone from reinventing the wheel ;)

Current Features:
- Event: StreamGoLiveEvent / StreamGoOffflineEvent / StreamChangeGameEvent / StreamChangeTitleEvent
- Event: FollowEvent

Those methods require the `Helix Module`, so enable it in the `TwitchClientBuilder`.

## Default Credential

When using the client helper, you will need to specify a default credential (any) that gets used for all requests for increased ratelimits, this is mandatory!

```java
TwitchClient twitchClient = TwitchClientBuilder.builder()
    .withDefaultAuthToken(new OAuth2Credential("twitch", "oauthtokenhere"))
    .build();
```

## Register for Stream GoLive / GoOffline / GameChange / TitleChange Events

```java
twitchClient.getClientHelper().enableStreamEventListener("twitch4j");
```

## Register for Follow Event

```java
twitchClient.getClientHelper().enableFollowEventListener("twitch4j");
```
