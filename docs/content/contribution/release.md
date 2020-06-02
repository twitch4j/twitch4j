+++
title="Releases"
weight = 50
+++

# Releases

The release process is fully automated using GitLab CI and gets triggered by git tag creation.

## Releasenotes

The releasenotes are generated using [EnvCLI/Modular-Pipeline](https://github.com/EnvCLI/modular-pipeline).

### Discord Announcements

The project contains a CI Variable called `RELEASE_WEBHOOK_DISCORD` which points to a discord webhook on the twitch4j announcements channel.


## Conventions

To generate a human readable changelogs the commits have to follow a very specific convention:

