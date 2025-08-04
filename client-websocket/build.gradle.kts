dependencies {
	// Common
	api(project(":twitch4j-util"))

	// WebSocket
	implementation(libs.nv.websocket.client)
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J Client - WebSocket")
	artifactDescription.set("WebSocket Client for Twitch4J modules")
	javadocTitle.set("Twitch4J (v${version}) - Client - WebSocket")
}
