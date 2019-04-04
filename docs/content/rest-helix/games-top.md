+++
title="Games - Top Games"
weight = 41
+++

# Get Top Games

## Description
Gets games sorted by number of current viewers on Twitch, most popular first.

The response has a JSON payload with a data field containing an array of games information elements and a pagination field containing information required to query for more streams.

## Method Definition

```java
@RequestLine("GET /games/top?after={after}&before={before}&first={first}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<GameTopList> getTopGames(
    @Param("token") String authToken,
	@Param("after") String after,
	@Param("before") String before,
	@Param("first") String first
);
```

*Required Parameters*

None

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Auth Token |
| after | string | Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query. |
| before | string | Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query. |
| first | string | Maximum number of objects to return. Maximum: 100. Default: 20. |

## Code-Snippets

### print game id and name for the top 10 games

```java
GameTopList resultList = twitchClient.getHelix().getTopGames(null, null, 10).execute();
resultList.getGames().forEach(game -> {
    System.out.println("ID: " + game.getId() + " - " + game.getName());
});
```
