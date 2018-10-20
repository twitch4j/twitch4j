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
    compile 'com.github.twitch4j:twitch4j:v0.13.0'
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
    <version>v0.13.0</version>
</dependency>
```

## fatJar

[Download](http://localhost)
