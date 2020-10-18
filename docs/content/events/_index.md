+++
title="Events"
weight = 200
+++

# Events

Twitch4J uses [Events4J](https://github.com/PhilippHeuer/events4j) to dispatch events and handle them, you can also build your own event handler to handle them however you like.
You can use those events to build chat bots or similar tools.

## Publish Event

Publish a event that should be processed somewhere:

* The event object needs to implement the IEvent interface.
* The event will be passed to all registered handlers.

```
eventManager.publish(object);
```

## Handle / Process Events

Twitch4J uses the `SimpleHandler` by default which works on all platforms.

Handlers:

- SimpleHandler
- ReactorHandler
- SpringHandler

You can register listeners directly on the eventManager, the call is forwarded and registered on the current `defaultEventHandler` set in the eventManager instance.

##### Switch the default event handler

If you want all your events to be processed by a specific eventHandler, then check out the following pages on all available eventHandlers:

* [SimpleEventHandler (Default)](./ceventhandler-simple)
* [ReactorEventHandler](./ceventhandler-reactor)

##### Register your event listeners in a generic way

Only the consumers registered with `eventManager.onEvent` will use the `defaultEventHandler`.

```java
// register handler
IDisposable handlerReg = twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
	System.out.println("[" + event.getChannel().getName() + "]["+event.getPermissions().toString()+"] " + event.getUser().getName() + ": " + event.getMessage());
});

// cancel handler (don't call the method for new events of the required type anymore)
handlerReg.dispose();
```

This is the recommended method to register listeners, as you can switch between the different EventHandlers by chaning a single line of code.

### Simple Handler

The default event handler can register `handlers` with annotations or provide lambdas as consumers.

###### Lambda / Consumer Example

```java
// register handler
IDisposable handlerReg = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelMessageEvent.class, event -> {
	System.out.println("[" + event.getChannel().getName() + "]["+event.getPermissions().toString()+"] " + event.getUser().getName() + ": " + event.getMessage());
});

// cancel handler (don't call the method for new events of the required type anymore)
handlerReg.dispose();
```

###### Annotation Example

```java
public class MyEventHandler {

    // the type of the 1st argument is relevant, you can pick any method name you want
    @EventSubscriber
    public void printChannelMessage(ChannelMessageEvent event) {
        System.out.println("[" + event.getChannel().getName() + "]["+event.getPermissions().toString()+"] " + event.getUser().getName() + ": " + event.getMessage());
    }

}

// register your handler class
MyEventHandler myEventHandler = new MyEventHandler();
eventManager.getEventHandler(SimpleEventHandler.class).registerListener(myEventHandler);
```

###### Cheat Sheet

| Key | Value |
|---|---|
| Handler Execution | sync |
| Android | compatible |

### Reactor Handler

If desired you can also use the `ReactorHandler` to use reactive streams to process events (keep in mind that this will not work with android).
To enable reactor add the `ReactorHandler` dependency, it will be discovered and registered automatically.

```
compile group: 'com.github.philippheuer.events4j', name: 'events4j-handler-reactor', version: '0.7.1'
```

```java
// register handler
Disposable handlerReg = twitchClient.getEventManager().getEventHandler(ReactorEventHandler.class).onEvent(ChannelMessageEvent.class, event -> {
	System.out.println("[" + event.getChannel().getName() + "]["+event.getPermissions().toString()+"] " + event.getUser().getName() + ": " + event.getMessage());
});

// cancel handler (don't call the method for new events of the required type anymore)
handlerReg.dispose();
```

###### Cheat Sheet

| Key | Value |
|---|---|
| Handler Execution | async |
| Android | incompatible |

### Spring Handler

If desired you can also use the `SpringHandler` to handle all events as Spring Application Events.
To enable reactor add the `SpringHandler` dependency, it will be discovered and registered automatically.

```
compile group: 'com.github.philippheuer.events4j', name: 'events4j-handler-spring', version: '0.7.1'
```

## Event Catalog

This will become a list of all available events.

Channel:
* [ChannelMessageEvent](./channel-message-event)
* [ChannelChangeGameEvent](./channel-change-game-event)
* [ChannelChangeTitleEvent](./channel-change-title-event)
* [ChannelGoLiveEvent](./channel-go-live-event)
* [ChannelGoOfflineEvent](./channel-go-offline-event)

User:
* [PrivateMessageEvent](./private-message-event)

Generic:
* [IRCMessageEvent](./irc-message-event)

*And many more, this is a very incomplete list*
