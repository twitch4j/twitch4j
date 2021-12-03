// Plugins
plugins {
	id("com.apollographql.apollo") version "2.5.11"
}

// Dependencies
dependencies {
	// GraphQL
	api(group = "com.apollographql.apollo", name = "apollo-runtime", version = "2.5.11")
	api(group = "org.jetbrains", name = "annotations")

	// Hystrix
	api(group = "com.netflix.hystrix", name = "hystrix-core")

	// Caching
	api(group = "com.github.ben-manes.caffeine", name = "caffeine")

	// Twitch4J Modules
	api(project(":common"))
	api(project(":auth"))
}

tasks.withType<io.freefair.gradle.plugins.lombok.tasks.Delombok> {
	dependsOn("generateMainServiceApolloSources")
}

tasks.withType<Javadoc> {
	// Ignore auto-generated files from apollo graphql
	exclude("com/github/twitch4j/graphql/internal/**")
}

base {
	archivesName.set("twitch4j-graphql")
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J GraphQL Module")
		description.set("GraphQL dependency")
	}
}
