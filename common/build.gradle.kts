projectConfiguration {
	artifactDisplayName.set("Twitch4J Common")
	artifactDescription.set("this module contains common code for all library modules")
}

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
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Websocket (for common proxy settings)
	compileOnly(group = "com.neovisionaries", name = "nv-websocket-client")

	// Rate-limit buckets for registry
	compileOnly(group = "com.github.vladimir-bukhtoyarov", name = "bucket4j-core")

	// Twitch4J Modules
	api(project(":twitch4j-auth"))
}
