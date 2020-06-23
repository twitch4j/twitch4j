+++
title="IDE - Development Environment"
weight = 5
+++

# Development Environment

We use lombok the automatically some parts of the java code, so you will have to setup the lombok plugin in your ide to compile / run `Twitch4J`.

## `Lombok`
This repository uses lombok to avoid the Getter/Setter boilerplate code in many classes.
Therefore you need to install Lombok for you IDE:

### IntelliJ - Plugin Installation

- Using IDE built-in plugin system on Windows:
  - <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "lombok"</kbd> > <kbd>Install Plugin</kbd>
- Using IDE built-in plugin system on MacOs:
  - <kbd>Preferences</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "lombok"</kbd> > <kbd>Install Plugin</kbd>
- Manually:
  - Download the [latest release](https://github.com/mplushnikov/lombok-intellij-plugin/releases/latest) and install it manually using <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>

Restart IDE.

### Eclipse - Plugin Installation

Download the latest `lombok.jar` from the [official Website](https://projectlombok.org/download.html).

Just run `lombok.jar` which will open the setup dialog, you need to specify your eclipse installation path here.

## Javadoc Generation

To generate the Javadocs in HTML5 you should use Java 9 or newer - the option will be set automatically based on your java version.
