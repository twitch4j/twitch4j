+++
title="Clips - Get"
weight = 31
+++

# Get Clips

## Description

Gets clip information by clip ID (one or more), broadcaster ID (one only), or game ID (one only).

The response has a JSON payload with a `data` field containing an array of clip information elements and a `pagination` field containing information required to query for more streams.

## Method Definition

```java
@RequestLine("GET /clips?broadcaster_id={broadcaster_id}&game_id={game_id}&id={id}&after={after}&before={before}&first={first}&started_at={started_at}&ended_at={ended_at}")
HystrixCommand<ClipList> getClips(
	@Param("broadcaster_id") Long broadcasterId,
	@Param("game_id") String gameId,
	@Param("id") String id,
	@Param("after") String after,
	@Param("before") String before,
	@Param("first") Integer limit,
	@Param("started_at") Date startedAt,
	@Param("ended_at") Date endedAt
);
```

*Required Parameters (one of)*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| broadcaster_id     | string    | ID of the broadcaster for whom clips are returned. The number of clips returned is determined by the `first` query-string parameter (default: 20). Results are ordered by view count. |
| game_id            | string    | ID of the game for which clips are returned. The number of clips returned is determined by the `first` query-string parameter (default: 20). Results are ordered by view count. |
| id         | string    | ID of the clip being queried. Limit: 100. |

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| after | string | Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. This applies only to queries specifying broadcaster_id or game_id. The cursor value specified here is from the pagination response field of a prior query. |
| before  | string | Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. This applies only to queries specifying broadcaster_id or game_id. The cursor value specified here is from the pagination response field of a prior query. |
| first | string | Ending date/time for returned clips, in RFC3339 format. (Note that the seconds value is ignored.) If this is specified, started_at also must be specified; otherwise, the time period is ignored. |
| started_at | string | Maximum number of objects to return. Maximum: 100. Default: 20. |
| ended_at | string | Starting date/time for returned clips, in RFC3339 format. (Note that the seconds value is ignored.) If this is specified, ended_at also should be specified; otherwise, the ended_at date/time will be 1 week after the started_at value. |

## Code-Snippets

### print clip id's

```java
ClipList clipList = twitchClient.getHelix().getClips(null, "488552", null, null, null, null, null, null).execute();
clipList.getData().forEach(clip -> {
    System.out.println("Found Clip: " + clip.getId());
});
```
