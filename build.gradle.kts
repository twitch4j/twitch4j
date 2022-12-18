// Plugins
plugins {
	signing
	`java-library`
	`maven-publish`
	id("io.freefair.lombok") version "6.6.1"
	id("com.coditory.manifest") version "0.2.3"
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
					"https://javadoc.io/doc/org.jetbrains/annotations/23.1.0",
					"https://javadoc.io/doc/commons-configuration/commons-configuration/1.10",
					"https://javadoc.io/doc/com.bucket4j/bucket4j_jdk8-core/8.1.0",
					// "https://javadoc.io/doc/com.squareup.okhttp3/okhttp/4.9.3", // blocked by https://github.com/square/okhttp/issues/6450
					"https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-core/0.11.0",
					"https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-handler-simple/0.11.0",
					"https://javadoc.io/doc/com.github.philippheuer.credentialmanager/credentialmanager/0.1.4",
					"https://javadoc.io/doc/io.github.openfeign/feign-slf4j/12.1",
					"https://javadoc.io/doc/io.github.openfeign/feign-okhttp/12.1",
					"https://javadoc.io/doc/io.github.openfeign/feign-jackson/12.1",
					"https://javadoc.io/doc/io.github.openfeign/feign-hystrix/12.1",
					"https://javadoc.io/doc/org.slf4j/slf4j-api/1.7.36",
					"https://javadoc.io/doc/com.neovisionaries/nv-websocket-client/2.14",
					"https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/2.14.1",
					"https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-core/2.14.1",
					"https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-annotations/2.14.1",
					"https://javadoc.io/doc/commons-io/commons-io/2.11.0",
					"https://javadoc.io/doc/org.apache.commons/commons-lang3/3.12.0",
					"https://javadoc.io/doc/org.projectlombok/lombok/1.18.24",
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
		version.set("1.18.24")
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
			api(group = "org.jetbrains", name = "annotations", version = "23.1.0")

			// Apache Commons
			api(group = "commons-configuration", name = "commons-configuration", version = "1.10")

			// Rate Limiting
			api(group = "com.bucket4j", name = "bucket4j_jdk8-core", version = "8.1.0")

			// HTTP
			api(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.3")

			// Credential Manager
			api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager", version = "0.1.4")

			// HTTP Client
			api(group = "io.github.openfeign", name = "feign-slf4j", version = "12.1")
			api(group = "io.github.openfeign", name = "feign-okhttp", version = "12.1")
			api(group = "io.github.openfeign", name = "feign-jackson", version = "12.1")
			api(group = "io.github.openfeign", name = "feign-hystrix", version = "12.1")

			// WebSocket
			api(group = "com.neovisionaries", name = "nv-websocket-client", version = "2.14")

			// Hystrix
			api(group = "com.netflix.hystrix", name = "hystrix-core", version = "1.5.18")

			// rich version declarations
			listOf("com.fasterxml.jackson.core:jackson-annotations", "com.fasterxml.jackson.core:jackson-core", "com.fasterxml.jackson.core:jackson-databind", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310").forEach { dep ->
				add("api", dep) {
					version {
						strictly("[2.12,3-alpha[")
						// renovate: depName=com.fasterxml.jackson:jackson-bom
						prefer("2.14.1")
					}
				}
			}
		}

		// Apache Commons
		api(group = "commons-io", name = "commons-io", version = "2.11.0")
		api(group = "org.apache.commons", name = "commons-lang3", version = "3.12.0")

		// Cache BOM
		api(platform("io.github.xanthic.cache:cache-bom:0.1.2"))

		// Events4J BOM
		api(platform("com.github.philippheuer.events4j:events4j-bom:0.11.0"))

		// Logging
		api(group = "org.slf4j", name = "slf4j-api", version = "1.7.36")

		// Jackson BOM
		api(platform("com.fasterxml.jackson:jackson-bom:2.14.1"))

		// Test
		testImplementation(platform("org.junit:junit-bom:5.9.1"))
		testImplementation(group = "org.junit.jupiter", name = "junit-jupiter")
		// - Mocking
		testImplementation(platform("org.mockito:mockito-bom:4.10.0"))
		// - Await
		testImplementation(group = "org.awaitility", name = "awaitility", version = "4.2.0")
		// - Logging
		testImplementation(group = "ch.qos.logback", name = "logback-classic", version = "1.3.5")
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
