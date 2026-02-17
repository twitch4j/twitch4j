dependencies {
	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Annotations
	api(group = "org.jetbrains", name = "annotations")

	// Twitch4J Modules
	api(project(":twitch4j-eventsub-common"))
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
	api(project(":twitch4j-client-websocket"))

	// Testing
	testImplementation(group = "org.mockito", name = "mockito-core")
	testImplementation(group = "org.mockito", name = "mockito-junit-jupiter")
	testImplementation(group = "org.awaitility", name = "awaitility")
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J API - PubSub Module")
	artifactDescription.set("PubSub API dependency")
	javadocTitle.set("${base.archivesName.get()} (v${project.version})")

	// generate shaded jar
	shadow.set(true)
}

base {
	archivesName.set("twitch4j-pubsub")
}
