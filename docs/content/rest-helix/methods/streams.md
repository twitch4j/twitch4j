+++
title="Streams"
weight = 100
+++

# Get Streams

Gets information about active streams. Streams are returned sorted by number of current viewers, in descending order. Across multiple pages of results, there may be duplicate or missing streams, as viewers join and leave streams.

The response has a JSON payload with a data field containing an array of stream information elements and a pagination field containing information required to query for more streams.

## Authentication

None

## URL

GET https://api.twitch.tv/helix/streams

## Required Query String Parameters

None

## Optional Query String Parameters

Name 	Type 	Description
after 	string 	Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
before 	string 	Cursor for backward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query.
community_id 	string 	Returns streams in a specified community ID. You can specify up to 100 IDs.
first 	integer 	Maximum number of objects to return. Maximum: 100. Default: 20.
game_id 	string 	Returns streams broadcasting a specified game ID. You can specify up to 100 IDs.
language 	string 	Stream language. You can specify up to 100 languages.
user_id 	string 	Returns streams broadcast by one or more specified user IDs. You can specify up to 100 IDs.
user_login 	string 	Returns streams broadcast by one or more specified user login names. You can specify up to 100 names.

## Code-Snippet

```java

```
