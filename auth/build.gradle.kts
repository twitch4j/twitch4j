dependencies {
	// Credential Manager
	api(CREDENTIAL_MANAGER)
	//testImplementation(CREDENTIAL_MANAGER_EWS)

	// Http Client
	api(OKHTTP3)

	// Jackson
	api(JACKSON_DATABIND)
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J Auth Module")
		description.set("Authentication dependency")
	}
}
