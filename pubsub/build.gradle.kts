projectConfiguration {
	artifactDisplayName.set("Twitch4J PubSub")
	artifactDescription.set("contains a websocket client for PubSub")
}

dependencies {
	// WebSocket
	api(group = "com.neovisionaries", name = "nv-websocket-client")

	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Cache
	api(group = "com.github.ben-manes.caffeine", name = "caffeine")

	// Annotations
	api(group = "org.jetbrains", name = "annotations")

	// Twitch4J Modules
	api(project(":twitch4j-eventsub-common"))
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
}
