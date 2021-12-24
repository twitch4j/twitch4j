projectConfiguration {
	artifactDisplayName.set("Twitch4J Auth")
	artifactDescription.set("this module contains auth-related code for twitch")
}

dependencies {
	// Http Client
	api(group = "com.squareup.okhttp3", name = "okhttp")

	// Jackson
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")

	// Credential Manager
	api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager")
}
