+++
title="TMI - Get Chatters"
weight = 10
+++

# Get Chatters

## Description

Gets vip's, moderators, staff, admins and normal viewers that are currently present in the chat. (Viewers that use a embedded viewer without chat can't be tracked using this)

There is no other way to request information about current viewers, as such this is currently the only way to get a list of people watching a stream.

## Method Definition

```java
@RequestLine("GET /group/user/{channel}/chatters")
HystrixCommand<Chatters> getChatters(
	@Param("channel") String channelName
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| channelName | text | Target Channel Name |

*Optional Parameters*

None

## Code-Snippets

### print current viewers (chatters)

```java
Chatters chatters = twitchClient.getMessagingInterface().getChatters("lirik").execute();

System.out.println("Broadcaster: " + chatters.getBroadcaster());
System.out.println("VIPs: " + chatters.getVips());
System.out.println("Mods: " + chatters.getModerators());
System.out.println("Admins: " + chatters.getAdmins());
System.out.println("Staff: " + chatters.getStaff());
System.out.println("Viewers: " + chatters.getViewers());
System.out.println("All Viewers (sum of the above): " + chatters.getAllViewers());
```
