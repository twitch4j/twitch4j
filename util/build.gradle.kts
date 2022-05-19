tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Util"
		windowTitle = "Twitch4J (v${version}) - Util"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J Util")
		description.set("Twitch4J Utils")
	}
}
