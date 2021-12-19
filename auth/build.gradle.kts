dependencies {
	// Http Client
	api(group = "com.squareup.okhttp3", name = "okhttp")

	// Jackson
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")

	// Credential Manager
	api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager")
}

publishing.publications.withType<MavenPublication> {
	artifactId = "twitch4j-auth"
	pom {
		name.set("Twitch4J Auth Module")
		description.set("Authentication dependency")
	}
}
