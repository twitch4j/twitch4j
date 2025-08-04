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
	api(libs.jackson.datatype.jsr310)

	// Twitch4J Modules
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J API - Kraken Module")
	artifactDescription.set("Kraken API dependency")
	javadocTitle.set("Twitch4J (v${version}) - Kraken Module API <sup>(deprecated)</sup>")
}
