+++
title="Analytics - Game"
weight = 11
+++

# Get Game Analytics

Gets a URL that game developers can use to download analytics reports (CSV files) for their games. The URL is valid for 5 minutes. For detail about analytics and the fields returned, see the [Insights & Analytics](https://dev.twitch.tv/docs/insights/) guide.

The response has a JSON payload with a `data` field containing an array of games information elements and can contain a `pagination` field containing information required to query for more streams.

## Method Definition

```java
@RequestLine("GET /analytics/games?after={after}&ended_at={ended_at}&first={first}&game_id={game_id}&started_at={started_at}&type={type}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<GameAnalyticsList> getGameAnalyticUrl(
	@Param("token") String authToken,
	@Param("after") String after,
	@Param("first") Integer limit,
	@Param("game_id") Long gameId,
	@Param("type") String type,
	@Param("started_at") String startedAt,
	@Param("ended_at") String endedAt
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Auth Token |

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| after     | string    | Cursor for forward pagination: tells the server where to start fetching the next set of results, in a multi-page response. This applies only to queries without `extension_id`. If an `extension_id` is specified, it supersedes any cursor/offset combinations. The cursor value specified here is from the pagination response field of a prior query. |
| first         | string    | Maximum number of objects to return. Maximum: 100. Default: 20. |
| type         | string    | Type of analytics report that is returned. If this is specified, the response includes one URL, for the specified report type. If this is not specified, the response includes multiple URLs (paginated), one for each report type available for the authenticated user’s Extensions. Limit: 1. Valid values: "overview_v1", "overview_v2". Default: all report types for the authenticated user’s Extensions. |
| extension_id         | string    | Client ID value assigned to the extension when it is created. If this is specified, the returned URL points to an analytics report for just the specified extension. If this is not specified, the response includes multiple URLs (paginated), pointing to separate analytics reports for each of the authenticated user’s extensions. |
| started_at         | string    | Starting date/time for returned reports, in RFC3339 format with the hours, minutes, and seconds zeroed out and the UTC timezone: `YYYY-MM-DDT00:00:00Z`. This must be on or after January 31, 2018.<br /><br />If this is provided, `ended_at` also must be specified. If `started_at` is earlier than the default start date, the default date is used. Default: January 31, 2018 (overview_v2) or 90 days (overview_v1) before the report was issued. The file contains one row of data per day. |
| ended_at         | string    | Ending date/time for returned reports, in RFC3339 format with the hours, minutes, and seconds zeroed out and the UTC timezone: `YYYY-MM-DDT00:00:00Z`. The report covers the entire ending date; e.g., if `2018-05-01T00:00:00Z` is specified, the report covers up to `2018-05-01T23:59:59Z`. <br /><br />If this is provided, `started_at` also must be specified. If `ended_at` is later than the default end date, the default date is used. Default: 1-2 days before the request was issued (depending on report availability). |

## Code-Snippets

### print report url (to download the files)

```java
ExtensionAnalyticsList resultList =twitchClient.getHelix().getGameAnalyticUrl(accessToken, null, 10, null, null, null, null).execute();
resultList.getGameAnalytics().forEach(analytic -> {
	System.out.println("URL: " + analytic.getURL());
});
```
