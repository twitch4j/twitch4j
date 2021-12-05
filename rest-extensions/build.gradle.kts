// In this section you declare the dependencies for your production and test code
dependencies {
	// HTTP Client
	api(group = "io.github.openfeign", name = "feign-okhttp")
	api(group = "io.github.openfeign", name = "feign-jackson")
	api(group = "io.github.openfeign", name = "feign-slf4j")
	api(group = "io.github.openfeign", name = "feign-hystrix")
	api(group = "commons-configuration", name = "commons-configuration")

	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Twitch4J Modules
	api(project(":twitch4j-common"))
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Extensions Module API"
		windowTitle = "Twitch4J (v${version}) - Extensions Module API"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Extensions Module")
		description.set("Extensions API dependency")
	}
}
