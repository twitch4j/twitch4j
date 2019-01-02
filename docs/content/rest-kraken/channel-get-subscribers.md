+++
title="Channel - Get Subscriptions"
weight = 50
+++

# Get Channel Subscribers

## Description

Gets a list of users subscribed to a specified channel, sorted by the date when they subscribed.

## Method Definition

```java
@RequestLine("GET /channels/{channelId}/subscriptions?limit={limit}&offset={offset}&direction={direction}")
@Headers({
	"Authorization: OAuth {token}",
	"Accept: application/vnd.twitchtv.v5+json"
})
HystrixCommand<KrakenSubscriptionList> getChannelSubscribers(
	@Param("token") String authToken,
	@Param("channelId") Long channelId,
	@Param("limit") Integer limit,
	@Param("offset") Integer offset,
	@Param("direction") String direction
);
```

*Required Parameters*


| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken | string | Authentication Token with scope `channel_subscriptions` for the target channel |
| channelId | long | Target Channel Id |

*Optional Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| limit | integer | Maximum number of objects to return. Default: 25. Maximum: 100. |
| offset | integer | Object offset for pagination of results. Default: 0. |
| direction | string | Sorting direction. Valid values: asc, desc. Default: asc (oldest first). |

## Code-Snippets

### print 25 subscribers of a channel (first page)

```java
KrakenSubscriptionList resultList = twitchClient.getKraken().getChannelSubscribers("accessToken", channelId, null, null, null).execute();
resultList.getSubscriptions().forEach(sub -> {
	System.out.println(sub.toString());
});
```
