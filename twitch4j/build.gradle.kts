// In this section you declare the dependencies for your production and test code
dependencies {
	// Twitch4J Modules
	val thatProject = project
	rootProject.subprojects.filter { it != thatProject }.forEach {
		api(it)
	}

	// Cache
	api(group = "com.github.ben-manes.caffeine", name = "caffeine")

	// Jackson
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")
}

publishing.publications.withType<MavenPublication> {
	artifactId = "twitch4j"
	pom {
		name.set("Twitch4J")
		description.set("Core dependency")
	}
}
