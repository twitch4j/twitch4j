// In this section you declare the dependencies for your production and test code
dependencies {
	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Annotations
	api(group = "org.jetbrains", name = "annotations")

	// Metrics
	api("io.micrometer:micrometer-core")

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

base {
	archivesName.set("twitch4j-pubsub")
}

tasks {
	javadoc {
		options {
			title = "${base.archivesName.get()} (v${project.version})"
			windowTitle = "${base.archivesName.get()} (v${project.version})"
		}
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - PubSub Module")
		description.set("PubSub API dependency")
	}
}
