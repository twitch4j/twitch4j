+++
title="TMI - Get Hosts"
weight = 11
+++

# Get Hosts

## Description

This endpoint returns a "host" record for each channel ID provided. If the channel is not hosting anyone, the target_id 
and target_login fields will not be present.

## Method Definition

```java
@RequestLine("GET /hosts?include_logins=1&host={id}")
HystrixCommand<HostList> getHosts(
    @Param("id") List<String> channelIds
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| channelIds | text | A list containing a user ID for each channel to check. |

*Optional Parameters*

None

## Code-Snippets

### print who a channel is hosting

```java
HostList hosts = twitchClient.getMessagingInterface().getHosts(List.of("29829912")).execute();

hosts.getHosts().forEach(host -> {
    System.out.println(host.getHostDisplayName() + " hosting " + host.getTargetDisplayName());
});
```
