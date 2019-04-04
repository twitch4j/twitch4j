+++
title="Videos - Get"
weight = 70
+++

# Get Videos

## Description

Gets video information by video ID (one or more), user ID (one only), or game ID (one only).

The response has a JSON payload with a data field containing an array of video elements. For lookup by user or game, pagination is available, along with several filters that can be specified as query string parameters.

## Method Definition

```java
@RequestLine("GET /videos?id={id}&user_id={user_id}&game_id={game_id}&language={language}&period={period}&sort={sort}&type={type}&after={after}&before={before}&first={first}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<VideoList> getVideos(
    @Param("token") String authToken,
	@Param("id") String id,
	@Param("user_id") Long userId,
	@Param("game_id") Long gameId,
	@Param("language") String language,
	@Param("period") String period,
	@Param("sort") String sort,
	@Param("type") String type,
	@Param("after") String after,
	@Param("before") String before,
	@Param("first") Integer limit
);
```

*Required Parameters (one of)*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| id | string | ID of the video being queried. Limit: 100. If this is specified, you cannot use any of the optional query string parameters below. |
| user_id | string | ID of the user who owns the video. Limit 1. |
| game_id | string | ID of the game the video is of. Limit 1. |

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Auth Token |
| after | string | Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query. |
| before | string | Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query. |
| limit | string | Maximum number of objects to return. Maximum: 100. Default: 20. |
| language | string | Language of the video being queried. Limit: 1. |
| period | string | Period during which the video was created. Valid values: "all", "day", "week", "month". Default: "all". |
| sort | string | Sort order of the videos. Valid values: "time", "trending", "views". Default: "time". |
| type | string | Type of video. Valid values: "all", "upload", "archive", "highlight". Default: "all". |

## Code-Snippets

### print videos

```java
VideoList resultList = twitchClient.getHelix().getVideos(null, null, "488552", null, null, null, null, null, null, 100).execute();

resultList.getVideos().forEach(video -> {
	System.out.println(video.getId() + ": " + video.getTitle() + " - by: " + video.getUserName());
});
```
