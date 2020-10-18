+++
title="Bits - Get Cheermotes"
weight = 20
+++

# Get Cheermotes

## Description

Retrieves the list of available Cheermotes, animated emotes to which viewers can assign Bits, to cheer in chat. 


## Method Definition

```java
@RequestLine("GET /bits/cheermotes?broadcaster_id={broadcaster_id}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<CheermoteList> getCheermotes(
    @Param("token") String authToken,
    @Param("broadcaster_id") String broadcasterId
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | Auth Token |

*Optional Query String Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| broadcaster_id | string    | ID for the broadcaster who might own specialized Cheermotes. |

## Code-Snippets

### print cheermotes image urls by tiers

```java
CheermoteList cheermoteList = twitchClient.getHelix().getCheermotes(authToken, "41245072").execute();
cheermoteList.getCheermotes().forEach(cheermote -> {
    System.out.println(cheermote.getPrefix());
    cheermote.getTiers().forEach(tier -> {
        System.out.println(tier.getId() + " " + tier.getImages().getLight().getAnimatedImages().getSize40());
    });
});
```
