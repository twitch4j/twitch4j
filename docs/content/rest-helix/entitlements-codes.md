+++
title="Entitlements - Get Code Status"
weight = 20
+++

# Get Code Status

## Description

Gets the status of one or more provided codes. 


## Method Definition

```java
@RequestLine("GET /entitlements/codes?code={code}&user_id={user_id}")
@Headers("Authorization: Bearer {token}")
HystrixCommand<CodeStatusList> getCodeStatus(
    @Param("token") String authToken,
    @Param("code") List<String> code,
    @Param("user_id") Integer userId
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| authToken     | string    | App Access Token |
| code     | string    | The code to get the status of. |
| userId     | integer    | Represents a numeric Twitch user ID |

## Code-Snippets

### print code status

```java
CodeStatusList codeStatusList = twitchClient.getHelix().getCodeStatus(authToken, List.of("KUHXV-4GXYP-AKAKK"), 156900877).execute();
codeStatusList.getStatuses().forEach(codeStatus -> {
    System.out.println(codeStatus.getCode() + " " + codeStatus.getStatus());
});
```
