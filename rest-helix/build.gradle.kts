plugins {
	id("com.gradleup.shadow")
}

dependencies {
	// HTTP Client
	api(group = "io.github.openfeign", name = "feign-okhttp")
	api(group = "io.github.openfeign", name = "feign-jackson")
	api(group = "io.github.openfeign", name = "feign-slf4j")
	api(group = "commons-configuration", name = "commons-configuration")

	// Resilience4j
	api(platform("io.github.resilience4j:resilience4j-bom:2.3.0"))
	api(group = "io.github.resilience4j", name = "resilience4j-all")
	api(group = "io.github.resilience4j", name = "resilience4j-feign")
	api(group = "io.github.resilience4j", name = "resilience4j-micrometer")

	// Rate Limiting
	api(group = "com.bucket4j", name = "bucket4j_jdk8-core")

	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")

	// Cache
	api(group = "io.github.xanthic.cache", name = "cache-provider-caffeine")

	// Twitch4J Modules
	api(project(":twitch4j-eventsub-common"))
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Helix Module API"
		windowTitle = "Twitch4J (v${version}) - Helix Module API"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Helix Module")
		description.set("Helix API dependency")
	}
}
