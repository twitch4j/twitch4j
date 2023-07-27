dependencies {
	// internal modules
	api(project(":twitch4j-api"))

	// bom
	api(platform("io.github.resilience4j:resilience4j-bom:2.1.0"))

	// feign core
	implementation("io.github.openfeign:feign-core")

	// resilience4J
	api("io.github.resilience4j:resilience4j-bulkhead")
	api("io.github.resilience4j:resilience4j-retry")
	api("io.github.resilience4j:resilience4j-circuitbreaker")
	api("io.github.resilience4j:resilience4j-ratelimiter")
	implementation("io.github.resilience4j:resilience4j-micrometer")
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Resilience4J"
		windowTitle = "Twitch4J (v${version}) - Resilience4J"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J Module - Resilience4J")
		description.set("Augments Twitch4J with Resilience4J")
	}
}

// overwrite required java version
java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}
