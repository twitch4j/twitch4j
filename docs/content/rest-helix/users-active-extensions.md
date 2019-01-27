+++
title="Users - Get Active Extensions"
weight = 64
+++

# Get User Active Extensions

## Description

Gets information about active extensions installed by a specified user, identified by a user ID or Bearer token.

## Method Definition

```java
@RequestLine("GET /users/extensions?user_id={user_id}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<ExtensionActiveList> getUserActiveExtensions(
	@Param("token") String authToken,
	@Param("user_id") Long userId
);
```

*Required Parameters*

None

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken | string | User Auth Token -> `user:read:broadcast` or `user:edit:broadcast` |
| user_id | string | ID of the user whose installed extensions will be returned. Limit: 1. |

## Code-Snippets

### print active extensions

```java
ExtensionActiveList resultList = twitchClient.getHelix().getUserActiveExtensions(testUtils.getCredential().getAccessToken(), twitchUserId).execute();

System.out.println(resultList.getData());
```
