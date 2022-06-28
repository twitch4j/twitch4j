// In this section you declare the dependencies for your production and test code
dependencies {
	// Common
	api(project(":twitch4j-util"))

	// WebSocket
	implementation(group = "com.neovisionaries", name = "nv-websocket-client")

	// Metrics
	api("io.micrometer:micrometer-core")
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Client - WebSocket"
		windowTitle = "Twitch4J (v${version}) - Client - WebSocket"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J Client - WebSocket")
		description.set("WebSocket Client for Twitch4J modules")
	}
}
