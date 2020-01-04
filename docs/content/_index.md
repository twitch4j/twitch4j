+++
title = "Twitch4J"
description = ""
+++

# Twitch4J

## Overview

* [Getting Started](./getting-started)
* [Events](./events)
* [Twitch4J](./twitch4j)

## Modules

Official
* [API - Helix](./rest-helix)
* [PubSub](./pubsub)
* [Chat](./chat)

Unofficial

* [API - Messaging Interface](./rest-tmi)
* [API - GraphQL](./graphql)

Deprecated / Scheduled for removal

* [API - Kraken](./rest-kraken)

## Events

Many modules can generate events which you can handle in some way, like PubSub, Chat, ...

For a list of available events / information on how to listen to events please check out: [Events](./events)

## Results of API Calls

This API Client provides you 3 methods to handle / process the results of api calls.

<details>
  <summary>Show</summary>
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
Observable<UserList> userListObservable = twitchClient.getKraken().getUsers(null, null, Arrays.asList("twitch4j")).observe();
```

You then retrieve the value of the command by subscribing to the Observable:

```java
userListObservable.subscribe(data -> {
  // your code to work with the result data
});
```
</details>

## Contribute to this documentation

Feel free to update this content, just edit the files in `docs/content` as usual markdown files and send a pull request.
