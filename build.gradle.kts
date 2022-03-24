// Plugins
plugins {
	signing
	`java-library`
	`maven-publish`
	id("io.freefair.lombok") version "6.4.1"
	id("com.coditory.manifest") version "0.1.14"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = group
version = version

// All-Projects
allprojects {
	// Repositories
	repositories {
		mavenCentral()
	}

	tasks {
		withType<Javadoc> {
			options {
				this as StandardJavadocDocletOptions
				links(
					"https://javadoc.io/doc/org.jetbrains/annotations/latest",
					"https://javadoc.io/doc/com.github.ben-manes.caffeine/caffeine/2.9.2",
					"https://commons.apache.org/proper/commons-configuration/javadocs/v1.10/apidocs",
					"https://javadoc.io/doc/com.github.vladimir-bukhtoyarov/bucket4j-core/7.3.0/",
					"https://square.github.io/okhttp/4.x/okhttp/",
					"https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-core/latest",
					"https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-handler-simple/latest",
					"https://javadoc.io/doc/com.github.philippheuer.credentialmanager/credentialmanager/latest",
					"https://javadoc.io/doc/io.github.openfeign/feign-slf4j/latest",
					"https://javadoc.io/doc/io.github.openfeign/feign-okhttp/latest",
					"https://javadoc.io/doc/io.github.openfeign/feign-jackson/latest",
					"https://javadoc.io/doc/io.github.openfeign/feign-hystrix/latest",
					"https://javadoc.io/doc/io.github.openfeign/feign-hystrix/latest",
					"https://netflix.github.io/Hystrix/javadoc/",
					"https://takahikokawasaki.github.io/nv-websocket-client/",
					"https://commons.apache.org/proper/commons-io/apidocs/",
					"https://commons.apache.org/proper/commons-lang/apidocs/",
					"https://www.javadoc.io/doc/org.slf4j/slf4j-api/1.7.36",
					"https://fasterxml.github.io/jackson-databind/javadoc/2.13/",
					"https://fasterxml.github.io/jackson-core/javadoc/2.13/",
					"https://fasterxml.github.io/jackson-annotations/javadoc/2.13/",
					"https://fasterxml.github.io/jackson-modules-java8/javadoc/datetime/2.13/",
					"https://projectlombok.org/api/",
					"https://twitch4j.github.io/javadoc"
				)
				locale = "en"
			}
		}
	}
}

// Subprojects
subprojects {
	apply(plugin = "signing")
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")
	apply(plugin = "io.freefair.lombok")
	apply(plugin = "com.coditory.manifest")
	apply(plugin = "com.github.johnrengelman.shadow")

	lombok {
		version.set("1.18.22")
		disableConfig.set(true)
	}

	// Source Compatibility
	java {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
		withSourcesJar()
		withJavadocJar()
	}

	// Dependency Management for Subprojects
	dependencies {
		constraints {
			// Annotations
			api(group = "org.jetbrains", name = "annotations", version = "23.0.0")

			// Caching
			api(group = "com.github.ben-manes.caffeine", name = "caffeine", version = "2.9.3")

			// Apache Commons
			api(group = "commons-configuration", name = "commons-configuration", version = "1.10")

			// Rate Limiting
			api(group = "com.github.vladimir-bukhtoyarov", name = "bucket4j-core", version = "7.3.0")

			// HTTP
			api(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.3")

			// Event Dispatcher
			api(group = "com.github.philippheuer.events4j", name = "events4j-core", version = "0.10.0")
			api(group = "com.github.philippheuer.events4j", name = "events4j-handler-simple", version = "0.10.0")

			// Credential Manager
			api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager", version = "0.1.2")

			// HTTP Client
			api(group = "io.github.openfeign", name = "feign-slf4j", version = "11.8")
			api(group = "io.github.openfeign", name = "feign-okhttp", version = "11.8")
			api(group = "io.github.openfeign", name = "feign-jackson", version = "11.8")
			api(group = "io.github.openfeign", name = "feign-hystrix", version = "11.8")

			// WebSocket
			api(group = "com.neovisionaries", name = "nv-websocket-client", version = "2.14")

			// Hystrix
			api(group = "com.netflix.hystrix", name = "hystrix-core", version = "1.5.18")

			// rich version declarations
			listOf("com.fasterxml.jackson.core:jackson-annotations", "com.fasterxml.jackson.core:jackson-core", "com.fasterxml.jackson.core:jackson-databind", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310").forEach { dep ->
				add("api", dep) {
					version {
						strictly("[2.12,3[")
						prefer("2.13.2")
					}
				}
			}
		}

		// Apache Commons
		api(group = "commons-io", name = "commons-io", version = "2.11.0")
		api(group = "org.apache.commons", name = "commons-lang3", version = "3.12.0")

		// Logging
		api(group = "org.slf4j", name = "slf4j-api", version = "1.7.36")

		// Jackson BOM
		api(platform("com.fasterxml.jackson:jackson-bom:2.13.2"))

		// Test
		testImplementation(platform("org.junit:junit-bom:5.8.2"))
		testImplementation(group = "org.junit.jupiter", name = "junit-jupiter")
		// - Mocking
		testImplementation(platform("org.mockito:mockito-bom:4.4.0"))
		// - Await
		testImplementation(group = "org.awaitility", name = "awaitility", version = "4.2.0")
		// - Logging
		testImplementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.11")
	}

	publishing {
		repositories {
			maven {
				name = "maven"
				url = uri(project.mavenRepositoryUrl)
				credentials {
					username = project.mavenRepositoryUsername
					password = project.mavenRepositoryPassword
				}
			}
		}
		publications {
			create<MavenPublication>("main") {
				from(components["java"])
				pom.default()
			}
		}
	}

	signing {
		useGpgCmd()
		sign(publishing.publications["main"])
	}

	// Source encoding
	tasks {
		// shadowjar & relocation
		val relocateShadowJar by creating(com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation::class) {
			target = shadowJar.get()
			prefix = "com.github.twitch4j.shaded.${"$version".replace(".", "_")}"
		}

		// jar artifact id and version
		withType<Jar> {
			if (this is com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar) {
				dependsOn(relocateShadowJar)
				archiveClassifier.set("shaded")
			}
			dependsOn(project.tasks.manifest)
			manifest.from(File(buildDir, "resources/main/META-INF/MANIFEST.MF"))
		}

		// compile options
		withType<JavaCompile> {
			options.encoding = "UTF-8"
		}

		// javadoc & delombok
		val delombok by getting(io.freefair.gradle.plugins.lombok.tasks.Delombok::class)
		javadoc {
			dependsOn(delombok)
			source(delombok)
			options {
				title = "${project.name} (v${project.version})"
				windowTitle = "${project.name} (v${project.version})"
				encoding = "UTF-8"
				overview = file("${rootDir}/buildSrc/overview-single.html").absolutePath
				this as StandardJavadocDocletOptions
				// hide javadoc warnings (a lot from delombok)
				addStringOption("Xdoclint:none", "-quiet")
				if (JavaVersion.current().isJava9Compatible) {
					// javadoc / html5 support
					addBooleanOption("html5", true)
				}
			}
		}

		// test
		test {
			useJUnitPlatform {
				includeTags("unittest")
				excludeTags("integration")
			}
		}
	}
}

tasks.register<Javadoc>("aggregateJavadoc") {
	enabled = JavaVersion.current().isJava9Compatible
	group = JavaBasePlugin.DOCUMENTATION_GROUP
	options {
		title = "${rootProject.name} (v${project.version})"
		windowTitle = "${rootProject.name} (v${project.version})"
		encoding = "UTF-8"
		this as StandardJavadocDocletOptions
		overview = file("${rootDir}/buildSrc/overview-general.html").absolutePath
		group("Common", "com.github.twitch4j.common*")
		group("Core", "com.github.twitch4j", "com.github.twitch4j.domain*", "com.github.twitch4j.events*", "com.github.twitch4j.modules*")
		group("Auth", "com.github.twitch4j.auth*")
		group("Chat", "com.github.twitch4j.chat*")
		group("EventSub", "com.github.twitch4j.eventsub*")
		group("PubSub", "com.github.twitch4j.pubsub*")
		group("Helix API", "com.github.twitch4j.helix*")
		group("Twitch Message Interface - API", "com.github.twitch4j.tmi*")
		group("GraphQL", "com.github.twitch4j.graphql*")
		group("Extensions API", "com.github.twitch4j.extensions*")
		group("Kraken API v5 (deprecated)", "com.github.twitch4j.kraken*")
		addBooleanOption("html5").setValue(true)
	}

	source(subprojects.map { it.tasks.javadoc.get().source })
	classpath = files(subprojects.map { it.tasks.javadoc.get().classpath })

	setDestinationDir(file("${rootDir}/docs/static/javadoc"))

	doFirst {
		if (destinationDir?.exists() == true) {
			destinationDir?.deleteRecursively()
		}
	}

	doLast {
		copy {
			from(file("${destinationDir!!}/element-list"))
			into(destinationDir!!)
			rename { "package-list" }
		}
	}
}
