dependencies {
	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Cache
	api(group = "io.github.xanthic.cache", name = "cache-provider-caffeine")

	// Twitch4J Modules
	api(project(":twitch4j-common"))
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - EventSub Common Module API"
		windowTitle = "Twitch4J (v${version}) - EventSub Common Module API"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - EventSub Common Module")
		description.set("EventSub Common dependency")
	}
}
