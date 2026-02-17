dependencies {
	// HTTP Client
	api(group = "io.github.openfeign", name = "feign-okhttp")
	api(group = "io.github.openfeign", name = "feign-jackson")
	api(group = "io.github.openfeign", name = "feign-slf4j")
	api(group = "io.github.openfeign", name = "feign-hystrix")
	api(group = "commons-configuration", name = "commons-configuration")

	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")

	// Twitch4J Modules
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J API - Message Interface Module")
	artifactDescription.set("Twitch Message Interface API dependency")
	javadocTitle.set("Twitch4J (v${version}) - Message Interface Module API <sup>(deprecated)</sup>")
}
