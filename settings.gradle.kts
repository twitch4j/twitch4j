rootProject.name = "Twitch4J"

include(
	":common",
	":auth",
	":client-websocket",
	":chat",
	":eventsub-common",
	":rest-extensions",
	":rest-helix",
	":rest-kraken",
	":rest-tmi",
	":pubsub",
	":graphql",
	":util",
	":twitch4j",
	":kotlin"
)

project(":common").name = "twitch4j-common"
project(":client-websocket").name = "twitch4j-client-websocket"
project(":auth").name = "twitch4j-auth"
project(":chat").name = "twitch4j-chat"
project(":eventsub-common").name = "twitch4j-eventsub-common"
project(":rest-extensions").name = "twitch4j-extensions"
project(":rest-helix").name = "twitch4j-helix"
project(":rest-kraken").name = "twitch4j-kraken"
project(":rest-tmi").name = "twitch4j-messaginginterface"
project(":pubsub").name = "twitch4j-pubsub"
project(":graphql").name = "twitch4j-graphql"
project(":util").name = "twitch4j-util"
project(":twitch4j").name = "twitch4j"
project(":kotlin").name = "twitch4j-kotlin"
