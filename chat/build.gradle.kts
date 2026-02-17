dependencies {
	// Rate Limiting
	api(libs.bucket4j.core)

	// Cache
	api(libs.xanthic.provider.caffeine3)

	// Named Capture Groups
	api(libs.named.regexp)

	// Twitch4J Modules
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
	api(project(":twitch4j-client-websocket"))

	// Testing
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.mockito:mockito-junit-jupiter")
	testImplementation("org.awaitility:awaitility")
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J Chat Module")
	artifactDescription.set("Chat dependency")
	javadocTitle.set("Twitch4J (v${version}) - Chat Module")

	// generate shaded jar
	shadow.set(true)
}
