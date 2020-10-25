+++
title="Moderators - Get"
weight = 45
+++

# Get Moderators

## Description

Returns all moderators in a channel.

## Method Definition

```java
@RequestLine(value = "GET /moderation/moderators?broadcaster_id={broadcaster_id}&user_id={user_id}&after={after}", collectionFormat = CollectionFormat.CSV)
@Headers("Authorization: Bearer {token}")
HystrixCommand<ModeratorList> getModerators(
    @Param("token") String authToken,
    @Param("broadcaster_id") String broadcasterId,
    @Param("user_id") List<String> userIds,
    @Param("after") String after
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Token for the broadcaster |
| broadcasterId | string    | Provided broadcaster_id must match the user_id in the auth token. |

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| userIds       | string    | Filters the results and only returns a status object for users who are moderators in this channel and have a matching user_id. |
| after         | string    | Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query. |

## Code-Snippets

### print list of moderators

```java
ModeratorList resultList = twitchClient.getHelix().getModerators(authToken, broadcasterId, null, null).execute();
resultList.getModerators().forEach(moderator -> {
    System.out.println(moderator);
});
```
