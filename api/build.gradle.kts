dependencies {
	// http client
	compileOnly("io.github.openfeign:feign-core")
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - API"
		windowTitle = "Twitch4J (v${version}) - API"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API")
		description.set("Twitch4J API")
	}
}
