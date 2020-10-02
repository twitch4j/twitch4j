+++
title="Entitlements - Redeem Code"
weight = 20
+++

# Redeem Code

## Description

Redeems one or more provided codes to the authenticated Twitch user.


## Method Definition

```java
@RequestLine("POST /entitlements/codes?code={code}&user_id={user_id}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<CodeStatusList> redeemCode(
    @Param("token") String authToken,
    @Param("code") List<String> code,
    @Param("user_id") Integer userId
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | User Auth Token |
| code     | string    | The code to redeem to the authenticated user’s account. |
| userId     | integer    | Represents a numeric Twitch user ID |

## Code-Snippets

### redeem code and print status

```java
CodeStatusList codeStatusList = twitchClient.getHelix().redeemCode(authToken, List.of("KUHXV-4GXYP-AKAKK"), 156900877).execute();
codeStatusList.getStatuses().forEach(codeStatus -> {
    System.out.println(codeStatus.getCode() + " " + codeStatus.getStatus());
});
```
