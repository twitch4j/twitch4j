+++
title="Users - Get"
weight = 100
+++

# Get Users

Gets information about one or more specified Twitch users. Users are identified by optional user IDs and/or login name. If neither a user ID nor a login name is specified, the user is looked up by Bearer token.

## Method

```java
/**
 * Get Users
 * <p>
 * Gets information about one or more specified Twitch users. Users are identified by optional user IDs and/or login name. If neither a user ID nor a login name is specified, the user is looked up by Bearer token.
 *
 * @param authToken Auth Token, optional, will include the users email address
 * @param userIds   User ID. Multiple user IDs can be specified. Limit: 100.
 * @param userNames User login name. Multiple login names can be specified. Limit: 100.
 * @return HelixUser
 */
@RequestLine("GET /users?id={id}&login={login}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<UserList> getUsers(
	@Param("token") String authToken,
	@Param("id") List<String> userIds,
	@Param("login") List<String> userNames
);
```

*Required Query String Parameters*

None

*Optional Query String Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | Can be used if requesting information about your own account, to include the email |
| id            | string    | User ID. Multiple user IDs can be specified. Limit: 100. |
| login         | string    | User login name. Multiple login names can be specified. Limit: 100. |

## Code-Snippets

### print user list - search by id

```java
UserList resultList = twitchClient.getHelix().getUsers(null, Arrays.asList("149223493"), null).execute();
resultList.getUsers().forEach(user -> {
	System.out.println(user);
});
```


### print user list - search by name

```java
UserList resultList = twitchClient.getHelix().getUsers(null, null, Arrays.asList("twitch4j")).execute();
resultList.getUsers().forEach(user -> {
	System.out.println(user);
});
```
