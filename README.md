<p align="center"><img src=".github/logo.png?raw=true" width="150"></p>

<p align="center">
<a href="https://travis-ci.org/twitch4j/twitch4j"><img src="https://travis-ci.org/twitch4j/twitch4j.svg?branch=master" alt="Build Status"></a>
<img src="https://img.shields.io/librariesio/github/twitch4j/twitch4j.svg?style=flat-square" alt="Libraries.io for GitHub">
<a href="https://www.codacy.com/app/twitch4j/twitch4j?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=twitch4j/twitch4j&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/4d9f9562de194b7f8699f9adfd0c4669" alt="Codacy Badge"></a>
<a href="http://isitmaintained.com/project/twitch4j/twitch4j"><img src="http://isitmaintained.com/badge/resolution/twitch4j/twitch4j.svg" alt="Average time to resolve an issue"></a>
<a href="http://isitmaintained.com/project/twitch4j/twitch4j"><img src="http://isitmaintained.com/badge/open/twitch4j/twitch4j.svg" alt="Percentage of issues still open"></a>
<a href="https://bintray.com/twitch4j/maven/Twitch4J/_latestVersion"><img src="https://api.bintray.com/packages/twitch4j/maven/Twitch4J/images/download.svg" alt="Download"></a>
</p>

# Java API for [Twitch](https://www.twitch.tv/)

Support:

[![Discord Server](https://discordapp.com/api/guilds/143001431388061696/embed.png?style=banner2)](https://discord.gg/FQ5vgW3)

[![Twitch API Server](https://discordapp.com/api/guilds/325552783787032576/embed.png?style=banner2)](https://discord.gg/8NXaEyV)

Documentation:

[![JavaDoc: Master](https://img.shields.io/badge/JavaDoc-Master-006400.svg?style=flat-square)](https://jitpack.io/com/github/PhilippHeuer/twitch4j/master-SNAPSHOT/javadoc/index.html)
[![JavaDoc: Develop](https://img.shields.io/badge/JavaDoc-Develop-006400.svg?style=flat-square)](https://jitpack.io/com/github/PhilippHeuer/twitch4j/develop-SNAPSHOT/javadoc/index.html)
[![Wiki](https://img.shields.io/badge/Wiki-Github-D3D3D3.svg?style=flat-square)](https://github.com/PhilippHeuer/twitch4j/wiki)

--------

## A quick note:
This API aims to retrieve all twitch relation information from the Twitch REST API V5, Twitch PubSub and the Twitch IRC Server. Futhermore twitch related services like Streamlabs are integrated.

This project is still in development, check out [Features](#features) and [Changelog](#changelog) to follow progress.

## Quick Start

#### Gradle
Add it to your build.gradle with:
```groovy
repositories {
	jcenter()
}
```
and: (latest, you should use the actual version here)

```groovy
dependencies {
    compile 'com.github.twitch4j:twitch4j:+'
}
```

#### Maven
Add it to your pom.xml with:
```xml
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
```
and: (latest, you should use the actual version here)

```xml
<dependency>
    <groupId>com.github.twitch4j</groupId>
    <artifactId>twitch4j</artifactId>
    <version>v0.10.0</version>
</dependency>
```

--------

## Features
#### OAuth Authentication
 - [x] Twitch
 - [x] StreamLabs
 
#### Twitch REST Endpoints
 - [ ] Bits
 - [ ] Channel Feed
 - [x] Channels
 - [ ] Chat
 - [ ] Clips
 - [ ] Collections
 - [x] Communities (Released: 10.02.2017)
 - [x] Games
 - [x] Ingests
 - [x] Search
 - [x] Streams
 - [x] Teams
 - [x] Users
 - [x] Videos

#### Twitch PubSub
 - [ ] Bits
 - [ ] Whispers
 - [ ] Subscriptions
 - [ ] Stream Status - *Unofficial*
 - [ ] Moderation Action (from specified moderator in specified channel) - *Unofficial*
 
#### Twitch IRC (WebSocket)
 - [x] Subscriptions
 - [x] Cheers (Bits)
 - [x] `/me` interactions chat
 - [x] `/color` changer
 - [x] [Moderation](https://help.twitch.tv/customer/portal/articles/659095-chat-moderation-commands)
   - [x] Ban (with reason)
   - [x] Timeout (with reason)
   - [x] Slow mode
   - [x] Sub mode
   - [x] Follow mode
   - [x] R9K mode
   - [x] Emote Only mode
 - [x] Channel Editor
   - [x] Commercial
   - [x] Host mode

#### 3rd Party
##### [Streamlabs](https://streamlabs.com/)
 - [x] Users (Read)
 - [x] Donations (Read, Create)
 - [x] Custom Alerts (Create)
 
##### [StreamTip](http://streamtip.com/)
 - Planned

##### [GameWisp](https://gamewisp.com/)
 - Planned
 
##### [StreamPro](https://streampro.io/)
  - No API

##### [Muxy.IO](https://muxy.io/)
  - No API

## Getting Started
Just some simple examples, visit the [WIKI](https://github.com/PhilippHeuer/twitch4j/wiki) for more details.

### Sample Projects using Twitch4J
 - [CLI: Chatbot - Twitch4J](https://github.com/twitch4j/twitch4j-chatbot) by @twitch4j
 - [GUI: Twitch Queue Bot](https://github.com/SirSkaro/twitch-queue-bot) by @SirSkaro

### Client Builder (Twitch Standalone)
```java
TwitchClient twitchClient = TwitchClientBuilder.init()
	.withClientId("Twitch App Id")
	.withClientSecret("Twitch App Secret")
	.withAutoSaveConfiguration(true)
	.withConfigurationDirectory(new File("config"))
	.withCredential("ixsxu9123xzmlx798xooa3f91q1e9c") // Get your token at: https://twitchapps.com/tmi/
	.connect();
```

### Get User Id from UserName
The Twitch V5 Endpoints prefer to use id's over names. You can get the user/channel id
by using the getUserIdByUserName Method of the UserEndpoint.
```java
// Channel Name to Channel ID (V5 API)
Optional<Long> userId = twitchClient.getUserEndpoint().getUserIdByUserName("whynabit");
```

Because there is the possibility that the user may not exists, you will get an optional return.
So you need to handle the result like this:
```java
if (userId.isPresent()) {
	// User exists
	System.out.println("UserID = " + userId.get());
} else {
	// User does not exist -> handle error
}
```

### Get Channel Follows
To extend the above example, the whole code to get the followers of a channel woud look like this:

```java
try {
	// Get Channel Endpoint for specific channel
	ChannelEndpoint channelEndpoint = twitchClient.getChannelEndpoint("channelName");

	// Get the 500 newest followers
	List<Follow> followList = channelEndpoint.getFollowers(Optional.ofNullable(500), Optional.ofNullable("desc"));
	for(Follow follow : followList) {
		System.out.println("User " + follow.getUser().getDisplayName() + " first followed at " + follow.getCreatedAt().toString());
	}

} catch (ChannelDoesNotExistException ex) {
	System.out.println("Channel does not exist!");
} catch (Exception ex) {
	ex.printStackTrace();
}
```

### WIKI
For more advanced features, check out the [WIKI](https://github.com/PhilippHeuer/twitch4j/wiki).

## Problems

If you have problems using the *Twitch Java API*, then you are welcome to join the [discord server](https://discord.gg/FQ5vgW3) to talk about it.

If you discover any issues/have feature requests, then please [open an issue here](https://github.com/PhilippHeuer/twitch4j/issues/new).

## License

Released under the [MIT license](./LICENSE).
