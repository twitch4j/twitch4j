// In this section you declare the dependencies for your production and test code
dependencies {
	// HTTP Client
	api(group = "io.github.openfeign", name = "feign-okhttp")
	api(group = "io.github.openfeign", name = "feign-jackson")
	api(group = "io.github.openfeign", name = "feign-slf4j")
	api(group = "io.github.openfeign", name = "feign-hystrix")
	implementation(group = "commons-configuration", name = "commons-configuration")

	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")

	// Twitch4J Modules
	api(project(":common"))
	api(project(":auth"))
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Message Interface Module")
		description.set("Twitch Message Interface API dependency")
	}
}
