<p align="center"><a href="https://twitch4j.github.io/"><img src=".github/logo.png?raw=true" width="150"></a></p>

# Java API for [Twitch](https://www.twitch.tv/)

Support:

[![Discord Server](https://discordapp.com/api/guilds/143001431388061696/embed.png?style=banner2)](https://discord.gg/FQ5vgW3)
[![Twitch API Server](https://discordapp.com/api/guilds/325552783787032576/embed.png?style=banner2)](https://discord.gg/8NXaEyV)

Badges:

[![Latest](https://img.shields.io/github/release/twitch4j/twitch4j/all.svg?style=flate&label=latest)](https://search.maven.org/search?q=g:com.github.twitch4j)
[![Docs](https://img.shields.io/badge/documentation-github%20pages-8A2BE2)](https://twitch4j.github.io/)
[![Javadoc](https://javadoc.io/badge/com.github.twitch4j/twitch4j.svg)](https://twitch4j.github.io/javadoc/)

--------

## A quick note:

This project implements client modules to interact with all of Twitch's APIs.

## Quick Start

Check out the [Documentation](https://twitch4j.github.io/getting-started/installation)!

--------

## Modules

**Project**

| Module             | Description                                  |
|--------------------|----------------------------------------------|
| chat               | Chat (IRC)                                   |
| eventsub-common    | EventSub (can be used for Webhook-Transport) |
| eventsub-websocket | EventSub (WebSocket & Conduits)              |
| helix              | REST-API                                     |
| pubsub             | PubSub                                       |
| graphql            | GraphQL (unofficial)                         |
| kotlin             | Kotlin Extensions                            |

**Shared**

| Module                                              | Description                        |
|-----------------------------------------------------|------------------------------------|
| [cache-api](https://github.com/Xanthic/cache-api)   | In-Memory Cache API                |
| [event4j](https://github.com/PhilippHeuer/events4j) | Event Publication and Subscription |

> The shared libraries offer standardized functionality in an abstracted manner, allowing you to select from a variety of implementations (or bring your own).

## Problems

If you have problems using the *Twitch Java API*, then you are welcome to join the [discord server](https://discord.gg/FQ5vgW3) to talk about it.

If you discover any issues/have feature requests, then please [open an issue here](https://github.com/twitch4j/twitch4j/issues/new).

## Contributing

We welcome contributions to the library, be it new features, bug fixes, or even small enhancements.
Please do read the [contributing guide](https://twitch4j.github.io/contribution/) on the documentation site as it provides code guidelines and helpful tips for getting started.
By contributing, you are expected to abide by our [code of conduct](https://github.com/twitch4j/.github/blob/main/CODE_OF_CONDUCT.md) and agree to the license below.
Thank you again for your interest in improving Twitch4J!

## License

Released under the [MIT license](./LICENSE).
