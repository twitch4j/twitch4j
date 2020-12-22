// In this section you declare the dependencies for your production and test code
dependencies {
	// Credential Manager
	api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager")
	//testImplementation(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager-ews")

	// Http Client
	api(group = "com.squareup.okhttp3", name = "okhttp")

	// Jackson
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J Auth Module")
		description.set("Authentication dependency")
	}
}
