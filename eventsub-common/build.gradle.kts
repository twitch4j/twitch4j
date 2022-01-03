projectConfiguration {
	artifactDisplayName.set("Twitch4J EventSub Common")
	artifactDescription.set("this module contains most of the eventSub domain objects")
}

dependencies {
	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Cache
	api(group = "com.github.ben-manes.caffeine", name = "caffeine")

	// Twitch4J Modules
	api(project(":twitch4j-common"))
}
