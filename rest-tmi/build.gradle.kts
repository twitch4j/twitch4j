// In this section you declare the dependencies for your production and test code
dependencies {
	// HTTP Client
	api(FEIGN_OKHTTP)
	api(FEIGN_JACKSON)
	api(FEIGN_SLF4J)
	api(FEIGN_HYSTRIX)
	implementation(COMMONS_CONFIGURATION)

	// Jackson (JSON)
	api(JACKSON_DATABIND)

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
