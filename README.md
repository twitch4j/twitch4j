# Java API for [Twitch](https://www.twitch.tv/) V5

Project Information:

[![Build Status](https://travis-ci.org/PhilippHeuer/twitch4j.svg?branch=master)](https://travis-ci.org/PhilippHeuer/twitch4j)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4d9f9562de194b7f8699f9adfd0c4669)](https://www.codacy.com/app/PhilippHeuer/twitch4j?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PhilippHeuer/twitch4j&amp;utm_campaign=Badge_Grade)
[![Dependency Status](https://www.versioneye.com/user/projects/5874cd85fff5dc002990c796/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5874cd85fff5dc002990c796)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/PhilippHeuer/twitch4j.svg)](http://isitmaintained.com/project/PhilippHeuer/twitch4j "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/PhilippHeuer/twitch4j.svg)](http://isitmaintained.com/project/PhilippHeuer/twitch4j "Percentage of issues still open")
[![](https://jitpack.io/v/philippheuer/twitch4j.svg)](https://jitpack.io/#philippheuer/twitch4j)

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

## Table of Contents
- [Features](#features)
- [Getting Started](#gettingstarted)
- [Problems](#problems)
- [Changelog](./CHANGELOG.md)
- [Credits](./CONTRIBUTORS.md)


## Features
#### OAuth Authentication
 - [x] Twitch
 - [x] StreamLabs
 
#### Twitch REST Endpoints
 - [ ] Channel Feed
 - [x] Channels
 - [ ] Chat
 - [x] Games
 - [ ] Ingests
 - [ ] Search
 - [x] Streams
 - [x] Teams
 - [ ] Users
 - [ ] Videos

#### Twitch PubSub
 - [ ] Bits
 - [ ] Whispers
 
#### Twitch IRC
 - [x] Subscriptions
 - [x] Cheers (Bits)
 
#### Streamlabs
 - [x] Users (Read)
 - [x] Donations (Read, Create)
 - [x] Custom Alerts (Create)
 
## Getting Started
Just some simple examples, visit the wiki for more details.

### Client Builder
```java
TwitchClient twitchClient = TwitchClient.builder()
	.clientId("***Twitch App Client ID***")
	.clientSecret("***Twitch App Client SECRET***")
	.configurationAutoSave(true)
	.configurationDirectory(new File("").getAbsolutePath())
	.build();
```

### Get Channel Follows
```java
// Channel Name to Channel ID (V5 API)
Optional<Long> channelId = twitchClient.getUserEndpoint().getUserIdByUserName("whynabit");

// Get the Channel Endpoint
if(channelId.isPresent()) {
	// Get Endpoint
	ChannelEndpoint channelEndpoint = twitchClient.getChannelEndpoint(channelId.get());
	
	// Get Follows
	// ...
} else {
	// User doesn't exist
}
```

### Event Listener
```java

```

```java

```

## Problems

If you have problems using the *Twitch Java API*, then you are welcome to join the [discord server](https://discord.gg/FQ5vgW3) to talk about it.

If you discover any issues/have feature requests, then please [open an issue here](https://github.com/PhilippHeuer/twitch4j/issues/new).

## License

Released under the [MIT license](./LICENSE).
