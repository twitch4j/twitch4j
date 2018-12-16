+++
title="PubSub"
weight = 15
+++

# Twitch PubSub

PubSub enables you to subscribe to a topic, for updates (e.g., when a user cheers on a channel).

The Twitch PubSub system allows back-end services to broadcast realtime messages to clients. Example applications include:

* An instant messaging service sending instant messages between friends.
* A back-end video system pushing real-time viewer count updates to video players.
* A presence system broadcasting users’ online status to all their friends.

## As Module: Twitch4J

To use PubSub you need to enable the PubSub when building the Twitch4J Instance, as shown below:

```java
TwitchClient twitchClient = TwitchClientBuilder.builder()
    ...
    .withEnablePubSub(true)
    ...
    .build();
```

## Standalone

Initialize the PubSub as Standalone Module:

```java
TwitchPubSub client = TwitchPubSubBuilder.builder().build();
```

## Consume the Results

Messages received over PubSub will be dispatched using the EventManager, please see the Twitch4J -> Events Documentation on how to listen for events or check out the individual pages below for specific code samples.

## Available Topics

In PubSub Context you subscribe to topics, a topic is for example `whispers to user twitch4j` or `subscriptions in channel twitch4j`.

Here is a list of available topics along with code samples:

WIP:

- [Bits Events](./topic-bits-events)
- [Subscribe Events](./topic-subscribe-events)
- [Commerce Events](./topic-commerce-events)

Complete:

- [Whispers](./topic-whispers)
