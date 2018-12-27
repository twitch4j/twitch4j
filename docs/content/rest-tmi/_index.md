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

## Consume the Results

This API Client provides you 3 methods to consume the results.

### Synchronous Execution

You can execute any api call synchronously with the execute() method, as shown in the following example:

```java
UserList userList = twitchClient.getHelix().getUsers(null, null, Arrays.asList("twitch4j")).execute();
```

### Asynchronous Execution

You can execute any api call asynchronously with the queue() method, as shown in the following example:

```java
Future<UserList> userListCall = twitchClient.getHelix().getUsers(null, null, Arrays.asList("twitch4j")).queue();
```

You can retrieve the result of the command by using the Future:

```java
UserList userList = userListCall.get();
```

### Reactive Execution

You can also observe the results of any api call as an Observable by using one of the following methods:

* observe() — returns a “hot” Observable that executes the command immediately, though because the Observable is filtered through a ReplaySubject you are not in danger of losing any items that it emits before you have a chance to subscribe
* toObservable() — returns a “cold” Observable that won’t execute the command and begin emitting its results until you subscribe to the Observable

```java
Observable<UserList> userListObservable = twitchClient.getHelix().getUsers(null, null, Arrays.asList("twitch4j")).observe();
```

You then retrieve the value of the command by subscribing to the Observable:

```java
userListObservable.subscribe(data -> {
    // your code to work with the result data
});
```

## API Methods

- [TMI -> Get Viewers](./get-viewers)
