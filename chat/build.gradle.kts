// In this section you declare the dependencies for your production and test code
dependencies {
	// WebSocket
	api(group = "com.neovisionaries", name = "nv-websocket-client")

	// Rate Limiting
	api(group = "com.github.vladimir-bukhtoyarov", name = "bucket4j-core")

	// Twitch4J Modules
	api(project(":common"))
	api(project(":auth"))
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J Chat Module")
		description.set("Chat dependency")
	}
}
