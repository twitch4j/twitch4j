+++
title="Clips - Create"
weight = 30
+++

# Create Clip

## Description

Creates a clip programmatically. This returns both an ID and an edit URL for the new clip.

Clip creation takes time. We recommend that you query Get Clips, with the clip ID that is returned here. If Get Clips returns a valid clip, your clip creation was successful. If, after 15 seconds, you still have not gotten back a valid clip from Get Clips, assume that the clip was not created and retry Create Clip.

This endpoint has a global rate limit, across all callers. The limit may change over time, but the response includes informative headers:

```
Ratelimit-Helixclipscreation-Limit: <int value>
Ratelimit-Helixclipscreation-Remaining: <int value>
```

## Method Definition

```java
@RequestLine("POST /clips?broadcaster_id={broadcaster_id}&has_delay={has_delay}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<CreateClipList> createClip(
	@Param("token") String authToken,
	@Param("broadcaster_id") String broadcasterId,
	@Param("has_delay") Boolean hasDelay
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Auth Token |

*Optional Query String Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| broadcaster_id | string    | ID of the stream from which the clip will be made. |
| has_delay      | string    | If `false`, the clip is captured from the live stream when the API is called; otherwise, a delay is added before the clip is captured (to account for the brief delay between the broadcaster’s stream and the viewer’s experience of that stream). Default: `false`. |

## Code-Snippets

### create clip and show id

```java
CreateClipList clipData = twitchClient.getHelix().createClip(accessToken, "149223493", false).execute();

clipData.getData().forEach(clip -> {
	System.out.println("Created Clip with ID: " + clip.getId());
});
```
