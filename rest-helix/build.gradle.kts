dependencies {
	// HTTP Client
	api(libs.feign.core)
	api(libs.feign.okhttp)
	api(libs.feign.jackson)
	api(libs.feign.slf4j)
	api(libs.feign.hystrix)
	api(libs.commons.configuration)

	// Jackson (JSON)
	api(libs.jackson.databind)

	// Rate-limit buckets for registry
	api(libs.bucket4j.core)

	// Cache
	api(libs.xanthic.provider.caffeine3)

	// Twitch4J Modules
	api(project(":twitch4j-eventsub-common"))
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J API - Helix Module")
	artifactDescription.set("Helix API dependency")
	javadocTitle.set("Twitch4J (v${version}) - Helix Module API")

	// generate shaded jar
	shadow.set(true)
}
