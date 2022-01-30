// In this section you declare the dependencies for your production and test code
dependencies {
	val versionKotlin = "1.6.0"

	// Twitch4J Modules
	// We use compileOnly so using this library does not require all modules.
	// This does mean that using code that references modules not available will crash.
	// This shouldn't be an issue as most, if not all, code are extension functions.
	compileOnly(project(":twitch4j"))

	// Kotlin coroutines
	api(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = versionKotlin)
	testImplementation(group = "org.jetbrains.kotlinx", name="kotlinx-coroutines-test", version = versionKotlin)
	testImplementation(project(":twitch4j"))
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Kotlin extension functions"
		windowTitle = "Twitch4J (v${version}) - Kotlin extension functions"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Kotlin extension functions")
		description.set("Twitch4J Kotlin extension functions")
	}
}


