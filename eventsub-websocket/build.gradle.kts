dependencies {
	// Twitch4J Modules
	api(project(":twitch4j-eventsub-common"))
	api(project(":twitch4j-helix"))
	api(project(":twitch4j-client-websocket"))
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - EventSub WebSocket Module"
		windowTitle = "Twitch4J (v${version}) - EventSub WebSocket Module"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - EventSub WebSocket Module")
		description.set("EventSub WebSocket dependency")
	}
}
