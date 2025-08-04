dependencies {
	// Twitch4J Modules
	val thatProject = project
	rootProject.subprojects
		.filter { it != thatProject }
		.filter { it.name != "twitch4j-kotlin" }
		.forEach {
			api(it)
		}

	api(libs.xanthic.provider.caffeine3)
	api(libs.jackson.databind)
	api(libs.named.regexp)
	testImplementation(libs.reflections)
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J Root Module")
	artifactDescription.set("Core dependency")
	javadocTitle.set("Twitch4J (v${version}) - Root Module API")

	// generate shaded jar
	shadow.set(true)
}

base {
	archivesName.set("twitch4j")
}
