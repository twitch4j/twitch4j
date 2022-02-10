// In this section you declare the dependencies for your production and test code
dependencies {
	// WebSocket
	api(group = "com.neovisionaries", name = "nv-websocket-client")

	// Rate Limiting
	api(group = "com.github.vladimir-bukhtoyarov", name = "bucket4j-core")

	// Cache
	api(group = "com.github.ben-manes.caffeine", name = "caffeine")

	// Twitch4J Modules
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))

	// Mocking
	testImplementation(group = "org.mockito", name = "mockito-core")
	testImplementation(group = "org.mockito", name = "mockito-junit-jupiter")
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Chat Module"
		windowTitle = "Twitch4J (v${version}) - Chat Module"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J Chat Module")
		description.set("Chat dependency")
	}
}
