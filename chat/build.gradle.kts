projectConfiguration {
	artifactDisplayName.set("Twitch4J Chat")
	artifactDescription.set("this module contains code to connect/use twitch chat")
}

dependencies {
	// WebSocket
	api(group = "com.neovisionaries", name = "nv-websocket-client")

	// Rate Limiting
	api(group = "com.github.vladimir-bukhtoyarov", name = "bucket4j-core")

	// Twitch4J Modules
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
}
