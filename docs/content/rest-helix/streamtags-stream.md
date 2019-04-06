+++
title="StreamTags - Get Stream Tags"
weight = 55
+++

# Get StreamTags of Stream

## Description

Gets the list of tags for a specified stream (channel).
The response has a JSON payload with a data field containing an array of tag elements.

## Method Definition

```java
@RequestLine("GET /streams/tags?broadcaster_id={broadcaster_id}")    
@Headers("Authorization: Bearer {token}")
HystrixCommand<StreamTagList> getStreamTags(
		@Param("token") String authToken,
		@Param("broadcaster_id") Long broadcasterId
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| broadcaster_id | string | ID of the stream thats tags are going to be fetched |

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Auth Token |

## Code-Snippets
