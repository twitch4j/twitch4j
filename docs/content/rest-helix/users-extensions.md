+++
title="Users - Get Extensions"
weight = 63
+++

# Get User Extensions

## Description

Gets a list of all extensions (both active and inactive) for a specified user, identified by a Bearer token.

## Method Definition

```java
@RequestLine("GET /users/extensions/list")
@Headers("Authorization: Bearer {token}")
HystrixCommand<ExtensionList> getUserExtensions(
	@Param("token") String authToken
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken | string | User Auth Token -> `user:read:broadcast` |

*Optional Parameters*

None

## Code-Snippets

### print all extensions

```java
ExtensionList resultList = twitchClient.getHelix().getUserExtensions(accessToken).execute();

resultList.getExtensions().forEach(extension -> {
    System.out.println(extension.toString());
});
```
