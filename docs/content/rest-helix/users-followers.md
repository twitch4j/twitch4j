+++
title="Users - Get Follows"
weight = 61
+++

# Get Users Follows

## Description

Gets information on follow relationships between two Twitch users. Information returned is sorted in order, most recent follow first. This can return information like “who is lirik following,” “who is following lirik,” or “is user X following user Y.”

The response has a JSON payload with a data field containing an array of follow relationship elements and a pagination field containing information required to query for more follow information.

## Method Definition

```java
@RequestLine("GET /users/follows?from_id={from_id}&to_id={to_id}&after={after}&first={first}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<FollowList> getFollowers(
    	@Param("token") String authToken,
	@Param("from_id") String fromId,
	@Param("to_id") String toId,
	@Param("after") String after,
	@Param("first") Integer limit
);
```

*Required Parameters*

None

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Auth Token |
| from_id | string | User ID. The request returns information about users who are being followed by the from_id user. |
| to_id | string | User ID. The request returns information about users who are following the to_id user. |
| after | string | Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. The cursor value specified here is from the pagination response field of a prior query. |
| limit | integer | Maximum number of objects to return. Maximum: 100. Default: 20. |

## Code-Snippets

### print followers

```java
FollowList resultList = twitchClient.getHelix().getFollowers(OAuth2.getAccessToken(), "149223493", null, null, 100).execute();

resultList.getFollows().forEach(follow -> {
    System.out.println(follow.getFromName() + " is following " + follow.getToName());
});
```
