// Buildscript
buildscript {
	repositories {
		jcenter()
		maven("https://maven.google.com")
	}
	dependencies {
		classpath("com.apollographql.apollo:apollo-gradle-plugin:1.2.2")
	}
}

// Plugins
apply(plugin = "com.apollographql.android")

// Dependencies
dependencies {
	// GraphQL
	api(group = "com.apollographql.apollo", name = "apollo-runtime", version = "1.2.2")
	api(group = "org.jetbrains", name = "annotations")

	// Hystrix
	api(group = "com.netflix.hystrix", name = "hystrix-core")

	// Twitch4J Modules
	api(project(":common"))
	api(project(":auth"))
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J GraphQL Module")
		description.set("GraphQL dependency")
	}
}
