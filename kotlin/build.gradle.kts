plugins {
	kotlin("jvm") version "2.0.21"
	id("org.jetbrains.dokka") version "1.9.20"
}

dependencies {
	// Twitch4J Modules
	// We use compileOnly so using this library does not require all modules.
	// This does mean that using code that references modules not available will crash.
	// This shouldn't be an issue as most, if not all, code are extension functions.
	compileOnly(project(":twitch4j"))

	api(group = "com.github.philippheuer.events4j", name = "events4j-kotlin")

	// Kotlin coroutines
	api(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.9.0")
	testImplementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version = "1.9.0")
	testImplementation(project(":twitch4j"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

tasks.javadocJar {
	from(tasks.dokkaJavadoc)
}

tasks.dokkaJavadoc {
	moduleName.set("Twitch4J (v${version}) - Kotlin extension functions")

	dokkaSourceSets {
		configureEach {
			jdkVersion.set(8)

			sourceLink {
				localDirectory.set(file("src/main/kotlin"))
				remoteUrl.set(uri("https://github.com/twitch4j/twitch4j/tree/master/kotlin/src/main/kotlin").toURL())
				remoteLineSuffix.set("#L")
			}

			externalDocumentationLink {
				url.set(uri("https://twitch4j.github.io/javadoc/").toURL())
			}
		}
	}
}

tasks.javadoc {
	options {
		exclude("**/*.kt")
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Kotlin extension functions")
		description.set("Twitch4J Kotlin extension functions")
	}
}
