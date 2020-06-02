+++
title="Releases"
weight = 50
+++

# Releases

The release process is fully automated using GitLab CI and gets triggered by git tag creation. There are no further actions required.

## Release Notes

The releasenotes are generated using [EnvCLI/Modular-Pipeline](https://github.com/EnvCLI/modular-pipeline).

### Discord Announcements

The project contains a CI Variable called `RELEASE_WEBHOOK_DISCORD` which points to a discord webhook on the twitch4j announcements channel.

### GitHub Releases

The project contains a GH_TOKEN thats used to create new GitHub Releases using the API, using the same changelog information as discord but with a different markup template.

## Conventions

To generate a human readable changelog, the commits have to follow the following naming convention:

| Commit Message        | Changelog Category  |
| ------------- :| -----------------:|
| feature: `message` | Features |
| feat: `message` | Features |
| fix: `message` | Bug Fixes |
| bugfix: `message` | Bug Fixes |
| perf: `message` | Performance Improvements |
| refactor: `message` | Code Refactoring |
| chore: `message` | Internal |
| docs: `message` | Documentation |
