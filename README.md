# Java API for [Twitch](https://www.twitch.tv/) V5

Project Information:

[![Build Status](https://travis-ci.org/PhilippHeuer/twitch4j.svg?branch=master)](https://travis-ci.org/PhilippHeuer/twitch4j)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4d9f9562de194b7f8699f9adfd0c4669)](https://www.codacy.com/app/PhilippHeuer/twitch4j?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PhilippHeuer/twitch4j&amp;utm_campaign=Badge_Grade)
[![Dependency Status](https://www.versioneye.com/user/projects/5874cd85fff5dc002990c796/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5874cd85fff5dc002990c796)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/PhilippHeuer/twitch4j.svg)](http://isitmaintained.com/project/PhilippHeuer/twitch4j "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/PhilippHeuer/twitch4j.svg)](http://isitmaintained.com/project/PhilippHeuer/twitch4j "Percentage of issues still open")
[![](https://jitpack.io/v/PhilippHeuer/twitch4j.svg)](https://jitpack.io/#PhilippHeuer/twitch4j)

Support:

[![Discord](https://img.shields.io/badge/Join-Twitch4J-7289DA.svg?style=flat-square)](https://discord.gg/FQ5vgW3)
[<img src="https://discordapp.com/api/guilds/143001431388061696/widget.png?style=shield">](https://discord.gg/FQ5vgW3)

--------

## A quick note:
This API aims to retrieve all twitch relation information from the Twitch REST API V5, Twitch PubSub and the Twitch IRC Server. Futhermore twitch related services like Streamlabs are integrated.

This project is still in development, check out [Features](#features) and [Changelog](#changelog) to follow progress.

## Quick Start
#### Gradle
Add it to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and:

```gradle
dependencies {
    compile 'com.github.PhilippHeuer:twitch4j:master-SNAPSHOT'
}
```

#### Maven
Add it to your pom.xml with:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
and:

```xml
<dependency>
    <groupId>com.github.PhilippHeuer</groupId>
    <artifactId>twitch4j</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

--------

## Features
#### OAuth Authentication
 - [x] Twitch
 - [x] StreamLabs
 
#### Twitch REST Endpoints
 - [ ] Channel Feed
 - [x] Channels
 - [ ] Chat
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
 
#### Twitch IRC
 - [x] Subscriptions
 - [x] Cheers (Bits)

#### 3rd Party
##### [Streamlabs](https://streamlabs.com/)
 - [x] Users (Read)
 - [x] Donations (Read, Create)
 - [x] Custom Alerts (Create)
 
##### [StreamTip](http://streamtip.com/)
 - Planned

##### [StreamPro](https://streampro.io/)
  - No API

##### [Muxy.IO](https://muxy.io/)
  - No API

## Getting Started
Just some simple examples, visit the [WIKI](https://github.com/PhilippHeuer/twitch4j/wiki) for more details.

### Sample Projects using Twitch4J
 - [Chatbot - Twitch4J](https://github.com/PhilippHeuer/twitch4j-chatbot)

### Client Builder (Twitch Standalone)
```java
TwitchClient twitchClient = TwitchClient.builder()
	.clientId("Twitch App ID")
	.clientSecret("Twitch App SECRET")
	.configurationAutoSave(true)
	.configurationDirectory(new File("").getAbsolutePath())
	.build();
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
