+++
title="Teams - Get By Name"
weight = 71
+++

# Get Team by Name

## Description

Gets a specified team object.

## Method Definition

```java
@RequestLine("GET /teams/{name}")
HystrixCommand<KrakenTeam> getTeamByName(
	@Param("name") String name
);
```

*Required Parameters (one of)*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| name | string | Name of the team |

*Optional Parameters*

None

## Code-Snippets

### get team by name

```java
KrakenTeam result = twitchClient.getKraken().getTeamByName("staff").execute();
System.out.println(result.getDisplayName());
```
