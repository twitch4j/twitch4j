projectConfiguration {
	artifactDisplayName.set("Twitch4J")
	artifactDescription.set("twitch4j main client with all available modules")
}

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
