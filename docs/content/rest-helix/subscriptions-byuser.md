+++
title="Subscriptions - Check subscription status of provided user IDs (one or more) for a given channel"
weight = 56
+++

# Check subscription status of provided user IDs (one or more) for a given channel

## Description

Gets broadcaster’s subscriptions by user ID (one or more).

Required OAuth Scope: `channel:read:subscriptions`

## Method Definition

```java
@RequestLine("GET /subscriptions?broadcaster_id={broadcaster_id}&user_id={user_id}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<SubscriptionList> getSubscriptionsByUser(
	@Param("token") String authToken,
	@Param("broadcaster_id") Long broadcasterId,
	@Param("user_id") List<Long> userIds
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| token | string | bearer token |
| broadcaster_id | number | ID of the broadcaster. Must match the User ID in the Bearer token |
| user_id | string | Returns streams broadcast by one or more specified user IDs. You can specify up to 100 IDs. |

*Optional Parameters*

*None*

## Code-Snippets

...
