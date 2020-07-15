+++
title="Installation"
weight = 2
+++

# Installation

We recommend using a dependency management tool to manage twitch4j and sub dependencies. As a fallback method you can also download a fatJar.

## Gradle
Add it to your build.gradle with:
```groovy
repositories {
    jcenter()
}
```
and: (latest, you should use the actual version here)

```groovy
dependencies {
    compile group: 'com.github.twitch4j', name: 'twitch4j', version: '1.1.0'
}
```

## Maven
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
    <version>1.1.0</version>
</dependency>
```
