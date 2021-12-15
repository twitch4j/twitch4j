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
	api(project(":twitch4j-eventsub-common"))
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
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
