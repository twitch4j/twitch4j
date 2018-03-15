<p align="center"><img src=".github/banner.png?raw=true" width="50%"></p>

<p align="center">
<a href="https://travis-ci.org/twitch4j/twitch4j"><img src="https://travis-ci.org/twitch4j/twitch4j.svg?branch=master" alt="Build Status"></a>
<img src="https://img.shields.io/librariesio/github/twitch4j/twitch4j.svg?style=flat-square" alt="Libraries.io for GitHub">
<a href="https://www.codacy.com/app/twitch4j/twitch4j?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=twitch4j/twitch4j&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/4d9f9562de194b7f8699f9adfd0c4669" alt="Codacy Badge"></a>
<a href="http://isitmaintained.com/project/twitch4j/twitch4j"><img src="http://isitmaintained.com/badge/resolution/twitch4j/twitch4j.svg" alt="Average time to resolve an issue"></a>
<a href="http://isitmaintained.com/project/twitch4j/twitch4j"><img src="http://isitmaintained.com/badge/open/twitch4j/twitch4j.svg" alt="Percentage of issues still open"></a>
<a href="https://bintray.com/twitch4j/maven/Twitch4J/_latestVersion"><img src="https://api.bintray.com/packages/twitch4j/maven/Twitch4J/images/download.svg" alt="Download"></a>
</p>

# Java API for [Twitch](https://www.twitch.tv/) V5

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

All features and bugs are moved into [Trello board](https://trello.com/b/c47tY7Eu/twitch4j)
If you looking for integrations or 3rd party modules check [Trello Board](https://trello.com/b/ujrssw8a) for our modules.

## Getting Started
Just some simple examples, visit the [WIKI](https://github.com/PhilippHeuer/twitch4j/wiki) for more details.

### Sample Projects using Twitch4J
 - [Chatbot - Twitch4J](https://github.com/PhilippHeuer/twitch4j-chatbot)

### Client Builder (Twitch Standalone)
```java
IClient client = Builder.newClient()
	.withClientId("<Client ID>")
	.withClientSecret("<Client Secret>")
	.withCredential(Builder.newCredential().withAccessToken("<Access Token>").withRefreshToken("<Refresh Token>")) // Get your token at: https://twitchapps.com/tmi/
	.connect();
```

### Get User
The Twitch V5 Endpoints prefer to use id's over names. You can get the user/channel id
by using the getUserIdByUserName Method of the UserEndpoint.
```java
// Gettting channel (Kraken)
Optional<User> userByName = client.getKrakenApi().userOperation().getByName("<User Name>");
User userById = client.getKrakenApi().userOperation().getById("<User ID>");
// Gettting channel (Helix)
Optional<HelixUser> userByName = client.getHelixApi().userOperation().getByName("<User Name>");
HelixUser userById = client.getHelixApi().userOperation().getById("<User ID>");
```

### Get Channel Follows
To extend the above example, the whole code to get the followers of a channel woud look like this:

```java
// Get Channel Endpoint for specific channel
Optional<Channel> channel = client.getKrakenApi().channelOpration().getByName("<Channel Name>");

channel.ifPresents(ch -> {
// Get the 500 newest followers
	List<Follow> followList = ch.getFollows(500, Sort.DESC);
	for(Follow follow : followList) {
    	System.out.println("User " + follow.getUser().getDisplayName() + " first followed at " + follow.createdAt().toString());
    }
});
```

### WIKI
For more advanced features, check out the [WIKI](https://github.com/PhilippHeuer/twitch4j/wiki).

## Problems

If you have problems using the *Twitch Java API*, then you are welcome to join the [discord server](https://discord.gg/FQ5vgW3) to talk about it.

If you discover any issues/have feature requests, then please [open an issue here](https://github.com/PhilippHeuer/twitch4j/issues/new).

## License

Released under the [MIT license](./LICENSE).
