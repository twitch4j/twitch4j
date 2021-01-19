// In this section you declare the dependencies for your production and test code
dependencies {
	// WebSocket
	api(NV_WEBSOCKET)

	// Jackson (JSON)
	api(JACKSON_DATATYPE_JSR310)

	// Cache
	api(CAFFEINE)

	// Annotations
	api(ANNOTATIONS)

	// Twitch4J Modules
	api(project(":common"))
	api(project(":auth"))
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - PubSub Module")
		description.set("PubSub API dependency")
	}
}
