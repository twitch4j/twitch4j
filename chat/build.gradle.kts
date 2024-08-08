plugins {
	id("com.gradleup.shadow")
}

dependencies {
	// Rate Limiting
	api(group = "com.bucket4j", name = "bucket4j_jdk8-core")

	// Cache
	api(group = "io.github.xanthic.cache", name = "cache-provider-caffeine")

	// Named Capture Groups
	api(group = "com.github.tony19", name = "named-regexp")

	// Twitch4J Modules
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
	api(project(":twitch4j-client-websocket"))

	// Testing
	testImplementation(group = "org.mockito", name = "mockito-core")
	testImplementation(group = "org.mockito", name = "mockito-junit-jupiter")
	testImplementation(group = "org.awaitility", name = "awaitility")
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
