+++
title="CredentialManager"
weight = 10
+++

# Twitch4J - CredentialManager

If you use any module that reqires auth directly, you need to setup your credentialManager and pass it into the builder on the `.withCredentialManager(credentialManager)` method.

The setup requires the TwitchAuth Module to run to register the twitch identity provider within the credentialManager.

## Example

```java
CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
credentialManager.registerIdentityProvider(new TwitchIdentityProvider("jzkbprff40iqj646a697cyrvl0zt2m6", "**SECRET**", ""));
```
