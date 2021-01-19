// In this section you declare the dependencies for your production and test code
dependencies {
	// Jackson (JSON)
	api(JACKSON_DATABIND)
	api(JACKSON_DATATYPE_JSR310)

	// Cache
	api(CAFFEINE)

	// Twitch4J Modules
	api(project(":common"))
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - EventSub Common Module")
		description.set("EventSub Common dependency")
	}
}
