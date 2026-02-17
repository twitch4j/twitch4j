dependencies {
	// Http Client
	api(group = "com.squareup.okhttp3", name = "okhttp")

	// Jackson
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")

	// Credential Manager
	api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager")
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J Auth Module")
	artifactDescription.set("Authentication dependency")
	javadocTitle.set("Twitch4J (v${version}) - Auth Module")
}
