+++
title="Bits - Leaderboard"
weight = 20
+++

# Get Bits Leaderboard

## Description

Gets a ranked list of Bits leaderboard information for an authorized broadcaster.

## Method Definition

```java
@RequestLine("GET /bits/leaderboard?count={count}&period={period}&started_at={started_at}&user_id={user_id}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<BitsLeaderboard> getBitsLeaderboard(
	@Param("token") String authToken,
	@Param("count") String count,
	@Param("period") String period,
	@Param("started_at") String startedAt,
	@Param("user_id") Long userId
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Auth Token |

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| count     | string    | Number of results to be returned. Maximum: 100. Default: 10. |
| period         | string    | Time period over which data is aggregated (PST time zone). This parameter interacts with `started_at`. Valid values are given below. Default: `"all"`.<br /><br />"day" – 00:00:00 on the day specified in started_at, through 00:00:00 on the following day.<br /*"week" – 00:00:00 on Monday of the week specified in started_at, through 00:00:00 on the following Monday.<br />"month" – 00:00:00 on the first day of the month specified in started_at, through 00:00:00 on the first day of the following month.<br />"year" – 00:00:00 on the first day of the year specified in started_at, through 00:00:00 on the first day of the following year.<br />"all" – The lifetime of the broadcaster's channel. If this is specified (or used by default), started_at is ignored. |
| started_at         | string    | Timestamp for the period over which the returned data is aggregated. Must be in RFC 3339 format. If this is not provided, data is aggregated over the current period; e.g., the current day/week/month/year. This value is ignored if period is "all".<br /><br />Any + operator should be URL encoded.<br /><br />Currently, the HH:MM:SS part of this value is used only to identify a given day in PST and otherwise ignored. For example, if the started_at value resolves to 5PM PST yesterday and period is "day", data is returned for all of yesterday. |
| user_id         | string    | ID of the user whose results are returned; i.e., the person who paid for the Bits.<br/>As long as count is greater than 1, the returned data includes additional users, with Bits amounts above and below the user specified by user_id.<br />If user_id is not provided, the endpoint returns the Bits leaderboard data across top users (subject to the value of count). |

## Code-Snippets

### print ranking

```java
BitsLeaderboard resultList = twitchClient.getHelix().getBitsLeaderboard(accessToken, "10", "all", null, null).execute();
resultList.getEntries().forEach(entry -> {
	System.out.println(entry.getRank() + ": " + entry.getUserId());
});
```
