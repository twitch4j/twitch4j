// In this section you declare the dependencies for your production and test code
dependencies {
	// Event Manager
	api(EVENTS_CORE)
	api(EVENTS_SIMPLE)

	// HTTP Client (for common feign extensions/interceptors/...)
	compileOnly(FEIGN_OKHTTP)
	compileOnly(FEIGN_JACKSON)
	compileOnly(FEIGN_SLF4J)
	compileOnly(FEIGN_HYSTRIX)

	// Jackson (JSON)
	api(JACKSON_DATATYPE_JSR310)

	// Websocket (for common proxy settings)
	compileOnly(NV_WEBSOCKET)

	// Twitch4J Modules
	api(project(":auth"))
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Common Module")
		description.set("Common API dependency")
	}
}
