+++
title="Chat"
weight = 4
+++

# Twitch API - Helix

Twitch offers an IRC interface to our chat functionality. This allows you to, for instance:

* Develop bots for your channel.
* Connect to a channel’s chat with an IRC client instead of using the Web interface.

While our IRC server generally follows RFC1459, there are several cases where it behaves slightly differently than other IRC servers; as described below, there are many Twitch-specific IRC capabilities. The differences are necessary to accommodate:

* The massive scale at which our chat servers operate, and
* Complex Twitch-specific features that we provide (to viewers, broadcasters, and developers).

## Twitch4J

To use Twitch Chat and events from chat, you need to specify withEnableChat when building the Twitch4J Instance, as shown below:

```java
// chat credential
OAuth2Credential credential = new OAuth2Credential("twitch", "oAuthTokenHere");

// twitch client
TwitchClient twitchClient = TwitchClientBuilder.builder()
            ...
            .withEnableChat(true)
            .withChatAccount(oAuth2CredentialHere)
            ...
            .build();
```

## Standalone

Initialize the Chat Feature as Standalone Module:

```java
TwitchChat client = TwitchChatBuilder.builder()
			.withChatAccount(oAuth2CredentialHere)
            .build();
```

The UserId is required, since it will be used to get the oauth credentials 

## Methods

* [JoinChannel](./join-channel.md)
* [LeaveChannel](./leave-channel.md)
