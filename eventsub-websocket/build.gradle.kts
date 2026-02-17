dependencies {
	// Twitch4J Modules
	api(project(":twitch4j-eventsub-common"))
	api(project(":twitch4j-helix"))
	api(project(":twitch4j-client-websocket"))
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J API - EventSub WebSocket Module")
	artifactDescription.set("EventSub WebSocket dependency")
	javadocTitle.set("Twitch4J (v${version}) - EventSub WebSocket Module")

	// generate shaded jar
	shadow.set(true)
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
	// Avoid com.github.twitch4j.eventsub.socket relocation due to eventsub-common being relocated
	relocate("com.github.twitch4j.eventsub.socket", "com.github.twitch4j.eventsub.socket")
}
