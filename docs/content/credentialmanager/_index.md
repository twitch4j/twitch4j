+++
title="CredentialManager"
weight = 10
+++

# Twitch4J - CredentialManager

If you use any module that reqires auth directly, you need to setup your credentialManager and pass it into the builder on the `.withCredentialManager(credentialManager)` method.

## Register the twitch identity provider using the AuthModule

```java

```

## Register the twitch identity provider manually

```java
CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
credentialManager.registerIdentityProvider(new TwitchIdentityProvider("jzkbprff40iqj646a697cyrvl0zt2m6", "**SECRET**", ""));
```
