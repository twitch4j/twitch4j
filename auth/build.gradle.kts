dependencies {
	// Http Client
	api(group = "com.squareup.okhttp3", name = "okhttp")

	// Jackson
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")

	// Credential Manager
	api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager")
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Auth Module"
		windowTitle = "Twitch4J (v${version}) - Auth Module"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J Auth Module")
		description.set("Authentication dependency")
	}
}
