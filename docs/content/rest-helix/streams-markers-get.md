+++
title="Streams - Get Markers"
weight = 53
+++

# Get Stream Markers

## Description

Gets a list of markers for either a specified user’s most recent stream or a specified VOD/video (stream), ordered by recency. A marker is an arbitrary point in a stream that the broadcaster wants to mark; e.g., to easily return to later. The only markers returned are those created by the user identified by the Bearer token.

The response has a JSON payload with a data field containing an array of marker information elements and a pagination field containing information required to query for more follow information.

## Method Definition

```java
@RequestLine("GET /streams/markers?after={after}&before={before}&first={first}&user_id={user_id}&video_id={video_id}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<StreamMarkersList> getStreamMarkers(
	@Param("token") String authToken,
	@Param("after") String after,
	@Param("before") String before,
	@Param("first") Integer limit,
	@Param("user_id") Long userId,
	@Param("video_id") Long videoId
);
```

*Required Parameters (one of)*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken | string | User Auth Token -> `user:read:broadcast` |
| user_id | string | ID of the broadcaster from whose stream markers are returned. |
| video_id | string | ID of the VOD/video whose stream markers are returned. |

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| after | string | Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query. |
| before | string | Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query. |
| limit | string | Maximum number of objects to return. Maximum: 100. Default: 20. |

## Code-Snippets

### print markers
```java
// TestCase
StreamMarkersList resultList = twitchClient.getHelix().getStreamMarkers(authToken, "", "", null, "217359661l", "137512364l").execute();
resultList.getStreamMarkers().forEach(stream -> {
	stream.getVideos().forEach(videoMarker -> {
		videoMarker.getMarkers().forEach(marker -> {
			System.out.println(marker.getId() + ":" + marker.getDescription())
		});
	});
});
```
