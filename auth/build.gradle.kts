dependencies {
	// Credential Manager
	api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager")
	//testImplementation(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager-ews", version = "0.1.1")

	// Http Client
	api(group = "com.squareup.okhttp3", name = "okhttp")

	// Jackson
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")
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
