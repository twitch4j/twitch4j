# Twitch Java API - V5

[![CircleCI](https://circleci.com/gh/PhilippHeuer/twitch4j/tree/master.svg?style=svg)](https://circleci.com/gh/PhilippHeuer/twitch4j/tree/master)
[![Dependency Status](https://www.versioneye.com/user/projects/5874cd85fff5dc002990c796/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5874cd85fff5dc002990c796)
[![Discord](https://img.shields.io/badge/Join-Twitch4J-7289DA.svg?style=flat-square)](https://discord.gg/FQ5vgW3)
[![](https://jitpack.io/v/philippheuer/twitch4j.svg)](https://jitpack.io/#philippheuer/twitch4j)

--------

## A quick note:
This API aims to retrieve all twitch relation information from the Twitch REST API V5, Twitch PubSub and the Twitch IRC Server. Futhermore twitch related services like StreamLabs are integrated.

This project is still in development, check out [Features](#features) and [Changelog](#changelog) to follow progress.

## Gradle
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
--------

## Table of Contents
- [Dependencies](#dependencies)
- [Libraries](#libraries)
- [Features](#features)
- [Changelog](#changelog)
- [Credits](#credits)
- [Examples](#examples)

## Dependencies
 * Java 1.8+
 
## Libraries
 * Spring Boot (Rest Requests)
 * Jackson (JSON)
 * Lombok (Getter & Setter)

## Features
#### OAuth Authentication
 - [ ] Twitch
 - [ ] StreamLabs
 
#### Twitch REST Endpoints
 - [ ] Channel Feed
 - [x] Channels
 - [ ] Chat
 - [x] Games
 - [ ] Ingests
 - [ ] Search
 - [X] Streams
 - [x] Teams
 - [ ] Users
 - [ ] Videos

#### Twitch PubSub
 - [ ] Bits
 - [ ] Whispers
 
#### Twitch IRC
 - [ ] Follows
 - [X] Subscriptions
 - [X] Cheers (Bits)
 
#### StreamLabs
 - [ ] Donations
 
## Changelog
Changes ... no release version yet.

## Credits
Click [here](CONTRIBUTORS.md) to view the projects contributors and credits.
 
## Examples
 - To Be Added
