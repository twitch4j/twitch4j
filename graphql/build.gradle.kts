// Plugins
plugins {
	apollo
}
// Dependencies
dependencies {
	// GraphQL
	api(APOLLO_RUNTIME)
	api(ANNOTATIONS)

	// Hystrix
	api(HYSTRIX)

	// Twitch4J Modules
	api(project(":common"))
	api(project(":auth"))
}

tasks.withType<Javadoc> {
	// Ignore auto-generated files from apollo graphql
	exclude("com/github/twitch4j/graphql/internal/**")
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J GraphQL Module")
		description.set("GraphQL dependency")
	}
}
