plugins {
	kotlin("jvm") version "1.6.21"
	id("org.jetbrains.dokka") version "1.6.21"
}

dependencies {
	// Twitch4J Modules
	// We use compileOnly so using this library does not require all modules.
	// This does mean that using code that references modules not available will crash.
	// This shouldn't be an issue as most, if not all, code are extension functions.
	compileOnly(project(":twitch4j"))

	api(group = "com.github.philippheuer.events4j", name = "events4j-kotlin", version = "0.10.0")

	// Kotlin coroutines
	api(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.6.1")
	testImplementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version = "1.6.1")
	testImplementation(project(":twitch4j"))
}

tasks.javadocJar {
	from(tasks.dokkaJavadoc)
}

tasks.dokkaJavadoc {
	moduleName.set("Twitch4J (v${version}) - Kotlin extension functions")
}

tasks.javadoc {
	enabled = false
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Kotlin extension functions")
		description.set("Twitch4J Kotlin extension functions")
	}
}
