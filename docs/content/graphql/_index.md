+++
title="GraphQL"
weight = 150
+++

# Twitch4J - GraphQL

*Experimental / Unofficial*

The GraphQL API is as unofficial as it can be. It only works by emulating the twitch website (`clientId` and `accessToken` matching the `clientId` of the twitch site itself).

Therefore you need to use the client id of the twitch site and a auth token of the twitch site - a self generated token will not work.

## As Module: Twitch4J

To use GraphQL you need to enable the GraphQL Module when building the Twitch4J Instance, as shown below:

```java
TwitchClient twitchClient = TwitchClientBuilder.builder()
    ...
    .withEnableGraphQL(true)
    ...
    .build();
```

## Standalone

Initialize the GraphQL as Standalone Module:

```java
TwitchGraphQL client = TwitchGraphQLBuilder.builder().build();
```

## API Methods

User:

- [User -> Follow User](./user-follow)
- [User -> Unfollow User](./user-unfollow)
