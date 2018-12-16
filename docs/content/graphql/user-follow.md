+++
title="User - Follow"
weight = 20
+++

# User - Follow

## Description

Follow a user on twitch (and turn of notifications if desired).

## Method Definition

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken | string | User Auth Token |
| targetUserId | number | Target User Id |
| notification | boolean | Enable notifications? |

*Optional Parameters*

None

## Code-Snippets

### follow a user (without notifications)

```java
twitchClient.getGraphQL().followUser(credential, 24943033l, false).execute();
```

### follow a user (with notifications)

```java
twitchClient.getGraphQL().followUser(credential, 24943033l, true).execute();
```
