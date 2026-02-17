plugins {
	kotlin("jvm")
}

dependencies {
	// Twitch4J Modules
	// We use compileOnly so using this library does not require all modules.
	// This does mean that using code that references modules not available will crash.
	// This shouldn't be an issue as most, if not all, code are extension functions.
	compileOnly(project(":twitch4j"))

	api(group = "com.github.philippheuer.events4j", name = "events4j-kotlin")

	// Kotlin coroutines
	api(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.10.2")
	testImplementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version = "1.10.2")
	testImplementation(project(":twitch4j"))
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J API - Kotlin extension functions")
	artifactDescription.set("Twitch4J Kotlin extension functions")
	javadocTitle.set("Twitch4J (v${version}) - Kotlin extension functions")

	// add source and javadoc links
	dokka = { dokka ->
		dokka.dokkaSourceSets.configureEach {
			sourceLink {
				localDirectory.set(file("src/main/kotlin"))
				remoteUrl.set(uri("https://github.com/twitch4j/twitch4j/tree/master/kotlin/src/main/kotlin"))
				remoteLineSuffix.set("#L")
			}

			externalDocumentationLinks.create("https://twitch4j.github.io/javadoc/") {
				url.set(uri("https://twitch4j.github.io/javadoc/"))
			}
		}
	}
}
