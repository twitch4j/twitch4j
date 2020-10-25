+++
title="Streams - Get Key"
weight = 51
+++

# Get Stream Key

## Description

Gets the channel stream key for a user.

## Method Definition

```java
@RequestLine("GET /streams/key?broadcaster_id={broadcaster_id}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<StreamKeyList> getStreamKey(
    @Param("token") String authToken,
    @Param("broadcaster_id") String broadcasterId
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Access Token (scope: channel:read:stream_key) |
| broadcasterId | string    | User ID of the broadcaster |

*Optional Parameters*

None

## Code-Snippets

### print stream key

```java
StreamKeyList resultList = twitchClient.getHelix().getStreamKey(authToken, broadcasterId).execute();
resultList.getKeys().forEach(key -> {
    System.out.println(key);
});
```
