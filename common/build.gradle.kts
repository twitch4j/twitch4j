// In this section you declare the dependencies for your production and test code
dependencies {
	// Event Manager
	api(group = "com.github.philippheuer.events4j", name = "events4j-core")
	api(group = "com.github.philippheuer.events4j", name = "events4j-handler-simple")

	// HTTP Client (for common feign extensions/interceptors/...)
	compileOnly(group = "io.github.openfeign", name = "feign-okhttp")
	compileOnly(group = "io.github.openfeign", name = "feign-jackson")
	compileOnly(group = "io.github.openfeign", name = "feign-slf4j")
	compileOnly(group = "io.github.openfeign", name = "feign-hystrix")

	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.module", name = "jackson-module-parameter-names")
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Websocket (for common proxy settings)
	compileOnly(group = "com.neovisionaries", name = "nv-websocket-client")

	// Twitch4J Modules
	api(project(":auth"))
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Common Module")
		description.set("Common API dependency")
	}
}
