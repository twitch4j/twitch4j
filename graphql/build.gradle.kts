// Plugins
plugins {
	alias(libs.plugins.apollo.plugin)
}

// Dependencies
dependencies {
	// Annotations
	api(libs.jetbrains.annotations)

	// GraphQL
	api(libs.apollo.runtime)

	// Hystrix
	api(libs.hystrix.core)

	// Caching
	api(libs.xanthic.provider.caffeine3)

	// Twitch4J Modules
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J GraphQL Module")
	artifactDescription.set("GraphQL dependency")
	javadocTitle.set("Twitch4J (v${version}) - GraphQL Module")
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
