plugins {
	id("com.gradleup.shadow")
}

dependencies {
	// Twitch4J Modules
	api(project(":twitch4j-eventsub-common"))
	api(project(":twitch4j-helix"))
	api(project(":twitch4j-client-websocket"))
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
	// Avoid com.github.twitch4j.eventsub.socket relocation due to eventsub-common being relocated
	relocate("com.github.twitch4j.eventsub.socket", "com.github.twitch4j.eventsub.socket")
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
