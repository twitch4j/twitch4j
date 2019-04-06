+++
title="StreamTags - Get All Stream Tags"
weight = 54
+++

# Get All Stream Tags

## Description

Gets the list of all stream tags defined by Twitch, optionally filtered by tag ID(s).

The response has a JSON payload with a data field containing an array of tag elements and a pagination field containing information required to query for more tags.

## Method Definition

```java
@RequestLine("GET /tags/streams?after={after}&first={first}&tag_id={tag_id}")    
@Headers("Authorization: Bearer {token}")
HystrixCommand<StreamTagList> getAllStreamTags(
		@Param("token") String authToken,
		@Param("after") String after,
		@Param("first") Integer limit,
		@Param("tag_id") List<UUID> tagIds
);
```

*Required Parameters*

None

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Auth Token |
| after | string | Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query. |
| limit | string | Maximum number of objects to return. Maximum: 100. Default: 20. |
| tagIds | string | ID of a tag. Multiple IDs can be specified, separated by ampersands. If provided, only the specified tag(s) is(are) returned. |

## Code-Snippets
