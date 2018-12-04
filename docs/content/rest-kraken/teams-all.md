+++
title="Teams - Get All"
weight = 70
+++

# Get All Teams

## Description

Gets all active teams.

## Method Definition

```java
@RequestLine("GET /teams?limit={limit}&offset={offset}")
HystrixCommand<KrakenTeamList> getAllTeams(
	@Param("limit") Long limit,
	@Param("offset") Long offset
);
```

*Required Parameters (one of)*

None

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| limit | number | Maximum number of objects to return, sorted by creation date. Default: 25. Maximum: 100. |
| offset | number | Object offset for pagination of results. Default: 0. |

## Code-Snippets

### print display name of all teams

```java
KrakenTeamList resultList = twitchClient.getKraken().getAllTeams(null, null).execute();
resultList.getTeams().forEach(team -> {
    System.out.println(team.getDisplayName());
});
```
