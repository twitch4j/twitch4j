plugins {
	id("com.apollographql.apollo") version "2.5.11"
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J GraphQL API (unofficial)")
	artifactDescription.set("contains a graphql client to call the unofficial graphql api")
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
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
}

tasks {
	withType<io.freefair.gradle.plugins.lombok.tasks.Delombok> {
		dependsOn("generateMainServiceApolloSources")
	}

	withType<Javadoc> {
		// Ignore auto-generated files from apollo graphql
		exclude("com/github/twitch4j/graphql/internal/**")
	}
}
