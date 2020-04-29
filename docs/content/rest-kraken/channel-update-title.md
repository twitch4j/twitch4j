+++
title="Channel - Update Title"
weight = 90
+++

# Update the title of a channel

## Description

Updates the channel / stream title

## Method Definition

```java
@RequestLine("PUT /channels/{channelId}?channel[status]={title}")
HystrixCommand<Object> updateTitle(
    @Param("token") String authToken,
    @Param("channelId") String channelId,
    @Param("title") String title
);
```

*Required Parameters (one of)*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| token | string | auth token |
| channelId | string | channel id |
| title | string | new channel title |

*Optional Parameters*

None

## Code-Snippets

### get team by name

```java
twitchClient.getKraken().updateTitle("authToken", "44322889", "Hello World!").execute();
```
