@file:Suppress("UnstableApiUsage")

// Plugins
plugins {
	signing
	`java-library`
	`maven-publish`
	id("io.freefair.lombok") version "5.3.3.3"
	id("com.github.johnrengelman.shadow") version "6.1.0"
}

// All-Projects
allprojects {
	// Repositories
	repositories {
		mavenCentral()
		mavenLocal()
		jcenter()
	}

	tasks {
		withType<Javadoc> {
			if (JavaVersion.current().isJava9Compatible) {
				(options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
			}
		}
		// prevent to generate 'lombok.config' - more about: https://projectlombok.org/features/configuration
		withType<io.freefair.gradle.plugins.lombok.tasks.GenerateLombokConfig> {
			enabled = false
		}
		// prevent to generate 'lombok.config' - more about: https://projectlombok.org/features/configuration
		withType<Jar> {
			archiveVersion.set("${project.version}")
		}
	}
}

// Subprojects
subprojects {
	apply(plugin = "signing")
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")
	apply(plugin = "io.freefair.lombok")
	apply(plugin = "com.github.johnrengelman.shadow")

	lombok {
		version.set("1.18.16")
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
			api(group = "org.jetbrains", name = "annotations", version = "20.1.0")

			// Caching
			api(group = "com.github.ben-manes.caffeine", name = "caffeine", version = "2.9.0")

			// Apache Commons
			api(group = "commons-configuration", name = "commons-configuration", version = "1.10")

			// Rate Limiting
			api(group = "com.github.vladimir-bukhtoyarov", name = "bucket4j-core", version = "4.7.0")

			// HTTP
			api(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.1")

			// Event Dispatcher
			api(group = "com.github.philippheuer.events4j", name = "events4j-core", version = "0.9.5")
			api(group = "com.github.philippheuer.events4j", name = "events4j-handler-simple", version = "0.9.5")
			api(group = "com.github.philippheuer.events4j", name = "events4j-handler-spring", version = "0.9.5")
			api(group = "com.github.philippheuer.events4j", name = "events4j-handler-reactor", version = "0.9.5")

			// Credential Manager
			api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager", version = "0.1.2")

			// HTTP Client
			api(group = "io.github.openfeign", name = "feign-slf4j", version = "11.1")
			api(group = "io.github.openfeign", name = "feign-okhttp", version = "11.1")
			api(group = "io.github.openfeign", name = "feign-jackson", version = "11.1")
			api(group = "io.github.openfeign", name = "feign-hystrix", version = "11.1")

			// WebSocket
			api(group = "com.neovisionaries", name = "nv-websocket-client", version = "2.14")

			// Hystrix
			api(group = "com.netflix.hystrix", name = "hystrix-core", version = "1.5.18")
		}

		// Apache Commons
		api(group = "commons-io", name = "commons-io", version = "2.8.0")
		api(group = "org.apache.commons", name = "commons-lang3", version = "3.12.0")

		// Logging
		api(group = "org.slf4j", name = "slf4j-api", version = "1.7.30")

		// Jackson BOM
		implementation(platform("com.fasterxml.jackson:jackson-bom:2.12.3"))

		// Test
		testImplementation(platform("org.junit:junit-bom:5.7.1"))
		testImplementation(group = "org.junit.jupiter", name = "junit-jupiter")
		testImplementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
	}

	publishing {
		repositories {
			maven {
				val releaseUri = uri("https://oss.sonatype.org/content/repositories/releases")
				val snapshotUri = uri("https://oss.sonatype.org/content/repositories/releases")
				name = "Nexus"
				url = if (project.isSnapshot) snapshotUri else releaseUri
				credentials {
					username = project.nexusUser
					password = project.nexusPassword
				}
			}
			maven {
				name = "GitHubPackages"
				url = uri("https://maven.pkg.github.com/twitch4j/twitch4j")
				credentials {
					username = project.githubRepoUser
					password = project.githubRepoToken
				}
				mavenContent {
					releasesOnly()
				}
			}
		}
		publications {
			create<MavenPublication>("main") {
				from(components["java"])
				artifactId = project.artifactId
				pom.default()
			}
		}
	}

	signing {
		sign(publishing.publications["main"])
	}

	// Source encoding
	tasks {
		withType<JavaCompile> {
			options.encoding = "UTF-8"
		}

		withType<Jar> {
			archiveBaseName.set(artifactId)
		}

		val delombok by getting(io.freefair.gradle.plugins.lombok.tasks.Delombok::class)
		val jar by getting(Jar::class)
		val relocateShadowJar by creating(com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation::class) {
			target = shadowJar.get()
			prefix = "com.github.twitch4j.shaded.${"$version".replace(".", "_")}"
		}

		javadoc {
			dependsOn(delombok)
			source(delombok)
			options {
				title = "${project.artifactId} (v${project.version})"
				windowTitle = "${project.artifactId} (v${project.version})"
				encoding = "UTF-8"
			}
		}

		shadowJar {
			dependsOn(relocateShadowJar)
			archiveClassifier.set("shaded")
			manifest.inheritFrom(jar.manifest)
		}

		test {
			useJUnitPlatform {
				includeTags("unittest")
				excludeTags("integration")
			}
		}
	}

//	bintray {
//		user = bintrayUser
//		key = bintrayApiKey
//		setPublications(*publishing.publications.filterIsInstance<MavenPublication>().map { it.name }.toTypedArray())
//		dryRun = false
//		publish = true
//		override = false
//		pkg.apply {
//			userOrg = "Twitch4J"
//			repo = "maven"
//			name = "Twitch4J"
//			version.apply {
//				name = "${project.version}"
//				vcsTag = "v${project.version}"
//				released = "${Date()}"
//			}
//		}
//	}
}

tasks.register<Javadoc>("aggregateJavadoc") {
	group = JavaBasePlugin.DOCUMENTATION_GROUP
	options {
		title = "${rootProject.name} (v${project.version})"
		windowTitle = "${rootProject.name} (v${project.version})"
		encoding = "UTF-8"
	}

	source(subprojects.map { it.tasks.delombok.get() })
	classpath = files(subprojects.map { it.sourceSets["main"].compileClasspath })

	setDestinationDir(file("${rootDir}/docs/static/javadoc"))
}
