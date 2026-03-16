import com.coditory.gradle.manifest.ManifestPluginExtension
import me.champeau.jmh.JmhParameters

plugins {
	`java-library`
	alias(libs.plugins.manifest.plugin).apply(false)
	alias(libs.plugins.jmh.plugin).apply(false)
	alias(libs.plugins.buildconfig.plugin).apply(false)
	alias(libs.plugins.configuration)
}

group = group
version = version

/**
 * Enables com.coditory.manifest plugin for `publish` tasks or if `-PenableManifest` is supplied trough cli
 */
val enableManifest = with(project) {
	gradle.startParameter.taskNames.any { s -> s.startsWith("publish") } || properties.containsKey("enableManifest")
}

allprojects {
	apply(plugin = "me.philippheuer.configuration")

	projectConfiguration {
		javaVersion.set(JavaVersion.VERSION_1_8)
		disablePluginModules.set(listOf("AutomaticModuleNameFeature", "DependencyReport"))

		pom = { pom ->
			pom.url.set("https://twitch4j.github.io")
			pom.issueManagement {
				system.set("GitHub")
				url.set("https://github.com/twitch4j/twitch4j/issues")
			}
			pom.inceptionYear.set("2017")
			pom.developers {
				developer {
					id.set("PhilippHeuer")
					name.set("Philipp Heuer")
					email.set("git@philippheuer.me")
					roles.addAll("maintainer")
				}
				developer {
					id.set("iProdigy")
					name.set("Sidd")
					roles.addAll("maintainer")
				}
			}
			pom.licenses {
				license {
					name.set("MIT Licence")
					distribution.set("repo")
					url.set("https://opensource.org/licenses/MIT")
				}
			}
			pom.scm {
				connection.set("scm:git:https://github.com/twitch4j/twitch4j.git")
				developerConnection.set("scm:git:git@github.com:twitch4j/twitch4j.git")
				url.set("https://github.com/twitch4j/twitch4j")
			}
		}

		javadocLint.set(listOf("none"))
		javadocOverviewTemplate.set("../javadoc/overview-single.html") // relative to module
		javadocOverviewAggregateTemplate.set("javadoc/overview-general.html") // relative to root
		javadocAggregateCustomize = { javadocOptions ->
			javadocOptions.group("Common", "com.github.twitch4j.common*")
			javadocOptions.group("Core", "com.github.twitch4j", "com.github.twitch4j.domain*", "com.github.twitch4j.events*", "com.github.twitch4j.modules*")
			javadocOptions.group("Auth", "com.github.twitch4j.auth*")
			javadocOptions.group("Chat", "com.github.twitch4j.chat*")
			javadocOptions.group("EventSub", "com.github.twitch4j.eventsub*")
			javadocOptions.group("PubSub", "com.github.twitch4j.pubsub*")
			javadocOptions.group("Helix API", "com.github.twitch4j.helix*")
			javadocOptions.group("Twitch Message Interface - API", "com.github.twitch4j.tmi*")
			javadocOptions.group("GraphQL", "com.github.twitch4j.graphql*")
			javadocOptions.group("Extensions API", "com.github.twitch4j.extensions*")
			javadocOptions.group("Kraken API v5 (deprecated)", "com.github.twitch4j.kraken*")
		}
	}
}

subprojects {
	apply(plugin = "java-library")
	apply(plugin = "me.champeau.jmh")

	if (enableManifest) {
		apply(plugin = "com.coditory.manifest")
		project.extensions
				.getByType(ManifestPluginExtension::class.java)
				.apply { buildAttributes = false }
	}

	project.extensions.getByType(JmhParameters::class).apply {
		iterations.set(4)
		fork.set(1)
	}

	// Dependency Management for Subprojects
	dependencies {
		// BOMs
		api(platform(rootProject.libs.xanthic.bom))
		api(platform(rootProject.libs.events4j.bom))
		api(platform(rootProject.libs.jackson.bom))
		api(platform(rootProject.libs.feign.bom))
		testImplementation(platform(rootProject.libs.junit.bom))
		testImplementation(platform(rootProject.libs.mockito.bom))

		// Apache Commons
		api(rootProject.libs.commons.io)
		api(rootProject.libs.commons.lang3)

		// Logging
		api(rootProject.libs.slf4j.api)
		testImplementation(rootProject.libs.logback.classic)

		// Testing
		testImplementation(rootProject.libs.junit.jupiter)
		testRuntimeOnly(rootProject.libs.junit.platform.launcher)
		testImplementation(rootProject.libs.awaitility)

		// Version Constraints
		constraints {
			// Annotations
			api(rootProject.libs.jetbrains.annotations)

			// Apache Commons
			api(rootProject.libs.commons.configuration)

			// Rate Limiting
			api(rootProject.libs.bucket4j.core)

			// Credential Manager
			api(rootProject.libs.credentialmanager)

			// HTTP Client
			api(rootProject.libs.okhttp)

			// WebSocket
			api(rootProject.libs.nv.websocket.client)

			// Regex
			api(rootProject.libs.named.regexp)

			// Hystrix
			api(rootProject.libs.hystrix.core)

			// rich version declarations
			listOf("com.fasterxml.jackson.core:jackson-annotations", "com.fasterxml.jackson.core:jackson-core", "com.fasterxml.jackson.core:jackson-databind", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310").forEach { dep ->
				add("api", dep) {
					version {
						strictly("[2.15,3-alpha[")

						if (dep.endsWith("-annotations")) {
							// renovate: depName=com.fasterxml.jackson.core:jackson-annotations
							prefer("2.21")
						} else {
							// renovate: depName=com.fasterxml.jackson:jackson-bom
							prefer("2.21.1")
						}
					}
				}
			}
			listOf("io.github.openfeign:feign-slf4j", "io.github.openfeign:feign-okhttp", "io.github.openfeign:feign-jackson", "io.github.openfeign:feign-hystrix").forEach { dep ->
				add("api", dep) {
					version {
						// lower bound on accepted feign version; synced with current major version used by twitch4j
						require("13.0")
					}
				}
			}
		}
	}

	// Source encoding
	tasks {
		// jar artifact id and version
		withType<Jar> {
			if (enableManifest) {
				manifest.from(File(buildDir, "resources/main/META-INF/MANIFEST.MF"))
			}
		}

		withType<Javadoc> {
			options {
				this as StandardJavadocDocletOptions
				links(
						"https://www.javadocs.dev/org.jetbrains/annotations/26.1.0",
						"https://www.javadocs.dev/commons-configuration/commons-configuration/1.10",
						"https://www.javadocs.dev/com.bucket4j/bucket4j_jdk8-core/8.10.1",
						// "https://www.javadocs.dev/com.squareup.okhttp3/okhttp/4.12.0", // blocked by https://github.com/square/okhttp/issues/6450
						"https://www.javadocs.dev/com.github.philippheuer.events4j/events4j-core/0.12.3",
						"https://www.javadocs.dev/com.github.philippheuer.events4j/events4j-handler-simple/0.12.3",
						"https://www.javadocs.dev/com.github.philippheuer.credentialmanager/credentialmanager/0.4.0",
						"https://www.javadocs.dev/io.github.openfeign/feign-slf4j/13.9.3",
						"https://www.javadocs.dev/io.github.openfeign/feign-okhttp/13.9.3",
						"https://www.javadocs.dev/io.github.openfeign/feign-jackson/13.9.3",
						"https://www.javadocs.dev/io.github.openfeign/feign-hystrix/13.9.3",
						"https://www.javadocs.dev/org.slf4j/slf4j-api/2.0.17",
						"https://www.javadocs.dev/com.neovisionaries/nv-websocket-client/2.14",
						"https://www.javadocs.dev/com.fasterxml.jackson.core/jackson-databind/2.21.1",
						"https://www.javadocs.dev/com.fasterxml.jackson.core/jackson-core/2.21.1",
						"https://www.javadocs.dev/com.fasterxml.jackson.core/jackson-annotations/2.21",
						"https://www.javadocs.dev/commons-io/commons-io/2.21.0",
						"https://www.javadocs.dev/org.apache.commons/commons-lang3/3.20.0",
						"https://www.javadocs.dev/org.projectlombok/lombok/1.18.42",
						"https://twitch4j.github.io/javadoc"
				)
			}
		}
	}
}
