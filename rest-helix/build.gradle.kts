// In this section you declare the dependencies for your production and test code
dependencies {
	// HTTP Client
	api(group = "io.github.openfeign", name = "feign-okhttp")
	api(group = "io.github.openfeign", name = "feign-jackson")
	api(group = "io.github.openfeign", name = "feign-slf4j")
	api(group = "io.github.openfeign", name = "feign-hystrix")
	api(group = "commons-configuration", name = "commons-configuration")

	// Rate Limiting
	api(group = "com.github.vladimir-bukhtoyarov", name = "bucket4j-core")

	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")

	// Cache
	api(group = "com.github.ben-manes.caffeine", name = "caffeine")

	// Twitch4J Modules
	api(project(":eventsub-common"))
	api(project(":common"))
	api(project(":auth"))
}

base {
	archivesName.set("twitch4j-helix")
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Helix Module")
		description.set("Helix API dependency")
	}
}
