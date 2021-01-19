// In this section you declare the dependencies for your production and test code
dependencies {
	// WebSocket
	api(NV_WEBSOCKET)

	// Rate Limiting
	api(BUCKET4J)

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
