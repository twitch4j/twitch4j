// In this section you declare the dependencies for your production and test code
dependencies {
	// HTTP Client
	api(FEIGN_OKHTTP)
	api(FEIGN_JACKSON)
	api(FEIGN_SLF4J)
	api(FEIGN_HYSTRIX)
	api(COMMONS_CONFIGURATION)

	// Jackson (JSON)
	api(JACKSON_DATABIND)
	api(JACKSON_DATATYPE_JSR310)

	// Twitch4J Modules
	api(project(":common"))
	api(project(":auth"))
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Kraken Module")
		description.set("Kraken API dependency")
	}
}
