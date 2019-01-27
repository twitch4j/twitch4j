+++
title="Games - Get"
weight = 40
+++

# Get Games

## Description

Gets game information by game ID or name.

The response has a JSON payload with a data field containing an array of games elements.

## Method Definition

```java
@RequestLine("GET /games?id={id}&name={name}")
HystrixCommand<GameList> getGames(
	@Param("id") List<Long> id,
	@Param("name") List<String> name
);
```

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| id | string | Game ID. At most 100 `id` values can be specified |
| name | string | Game name. The name must be an exact match. For instance, “Pokemon” will not return a list of Pokemon games; instead, query the specific Pokemon game(s) in which you are interested. At most `100` name values can be specified. |

*Optional Parameters*

None

## Code-Snippets

### print game name

```java
GameList resultList = twitchClient.getHelix().getGames(Arrays.asList(overwatchGameId), null).execute();

resultList.getGames().forEach(game -> {
    System.out.println("Game ID: " + game.getId() + " is " + game.getName());
});
```
