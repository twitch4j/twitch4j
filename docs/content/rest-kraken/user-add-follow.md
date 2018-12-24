+++
title="Users - Add Follow"
weight = 50
+++

# Follow Channel

## Description

Adds a specified user to the followers of a specified channel.

There is an error response (422 Unprocessable Entity) if the channel could not be followed.

## Method Definition

```java
@RequestLine("PUT /users/{user}/follows/channels/{targetUser}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<Object> addFollow(
	@Param("token") String authToken,
	@Param("user") Long userId,
	@Param("targetUser") Long targetUserId
);
```

*Required Parameters*

None

*Optional Parameters*

None

## Code-Snippets

### follow a channel

```java
twitchClient.getKraken().addFollow(auth, user, targetUserId).execute();
```
