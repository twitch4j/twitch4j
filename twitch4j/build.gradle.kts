// In this section you declare the dependencies for your production and test code
dependencies {
	// Twitch4J Modules
	val thatProject = project
	rootProject.subprojects
		.filter { it != thatProject }
		.filter { it.name != "twitch4j-kotlin" }
		.forEach {
			api(it)
		}

	// Cache
	api(group = "io.github.xanthic.cache", name = "cache-provider-caffeine")

	// Jackson
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")

	// Regex
	api(group = "com.github.tony19", name = "named-regexp")
}

base {
	archivesName.set("twitch4j")
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Root Module API"
		windowTitle = "Twitch4J (v${version}) - Root Module API"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J Root Module")
		description.set("Core dependency")
	}
}
