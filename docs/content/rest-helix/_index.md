# Twitch API - Helix

To make an application that uses the Twitch API, you first need to register your application on the Twitch developer site. When creating this app, enter your redirect URI, which is where your users are redirected after being authorized.

Once you create a developer application, you are assigned a client ID. Some authentication flows also require a client secret, which you can generate on the same page as the client ID.

* Client IDs are public and can be shared (for example, embedded in the source of a Web page).
* Client secrets are equivalent to a password for your application and must be kept confidential. Never expose it to users, even in an obscured form.

Because your client secret is confidential, we cannot show it to you once you leave the page, so make sure to record it somewhere safe. Also, generating a new client secret immediately invalidates the current one, which might make your API requests fail until your app is updated.

Warning: Client IDs should be unique for each application: do not re-use client IDs across multiple applications. Also, applications should request the appropriate set of scopes for the intended target APIs. Failure to adhere to these guidelines may result in the suspension of your application’s access to the Twitch API.

You can create new application on the [Twitch Developer Console](https://glass.twitch.tv/console/apps/create)

## Twitch4J

To use Helix you need to enable the Helix API when building the Twitch4J Instance, as shown below:

```
TwitchClient twitchClient = TwitchClientBuilder.builder()
            ...
            .withEnableHelix(true)
            ...
            .build();
```

## Standalone

Initialize the Helix API as Standalone Module:

```java
TwitchHelix client = TwitchHelixBuilder.builder()
            .withClientId("clientId")
            .withClientSecret("clientSecret")
            .build();
```
