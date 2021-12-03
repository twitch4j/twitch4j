// In this section you declare the dependencies for your production and test code
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
	api(project(":eventsub-common"))
	api(project(":common"))
	api(project(":auth"))
}

base {
	archivesName.set("twitch4j-pubsub")
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - PubSub Module")
		description.set("PubSub API dependency")
	}
}
