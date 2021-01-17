import java.util.Date
import java.util.jar.JarFile

// Plugins
plugins {
	`java-library`
	`maven-publish`
	id("com.jfrog.bintray") version "1.8.5"
	id("io.freefair.lombok") version "5.3.0"
	id("com.github.johnrengelman.shadow") version "6.1.0"
}

// All-Projects
allprojects {
	// Repositories
	repositories {
		jcenter()
//		mavenLocal()
	}

	tasks {
		withType<Javadoc> {
			if (JavaVersion.current().isJava9Compatible) { // to use HTML5 javadoc must be compatible to JDK9+
				(options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
			}
		}
		// prevent to generate 'lombok.config' - more about: https://projectlombok.org/features/configuration
		withType<io.freefair.gradle.plugins.lombok.tasks.GenerateLombokConfig> {
			enabled = false
		}
	}
}

// Subprojects
subprojects {
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")
	apply(plugin = "com.jfrog.bintray")
	apply(plugin = "io.freefair.lombok")
	apply(plugin = "com.github.johnrengelman.shadow")

	lombok {
		setVersion("1.18.16")
	}

	/*
	 * java version requirements and
	 * generate tasks sourcesJar and javadocJar
	 */
	extensions.configure<JavaPluginExtension> {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
		withSourcesJar()
		withJavadocJar()
	}

	// Dependency Management for Subprojects
	dependencies {
		constraints {
			// Logging
			api(group = "org.slf4j", name = "slf4j-api", version = "1.7.30")
			api(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")

			// Apache Commons
			api(group = "org.apache.commons", name = "commons-lang3", version = "3.11")
			api(group = "commons-io", name = "commons-io", version = "2.8.0")
			api(group = "commons-configuration", name = "commons-configuration", version = "1.10")

			// Event Dispatcher
			api(group = "com.github.philippheuer.events4j", name = "events4j-core", version = "0.9.5")
			api(group = "com.github.philippheuer.events4j", name = "events4j-handler-simple", version = "0.9.5")
			api(group = "com.github.philippheuer.events4j", name = "events4j-handler-reactor", version = "0.9.5")
			api(group = "com.github.philippheuer.events4j", name = "events4j-handler-spring", version = "0.9.5")

			// Credential Manager
			api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager", version = "0.1.1")

			// HTTP Client
			api(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.0")

			// Rate Limiting
			api(group = "com.github.vladimir-bukhtoyarov", name = "bucket4j-core", version = "4.7.0")

			// Caching
			api(group = "com.github.ben-manes.caffeine", name = "caffeine", version = "2.8.6")

			// Http Client
			api(group = "io.github.openfeign", name = "feign-okhttp", version = "11.0")
			api(group = "io.github.openfeign", name = "feign-jackson", version = "11.0")
			api(group = "io.github.openfeign", name = "feign-slf4j", version = "11.0")
			api(group = "io.github.openfeign", name = "feign-hystrix", version = "11.0")

			// Hystrix
			api(group = "com.netflix.hystrix", name = "hystrix-core", version = "1.5.18")

			// Jackson (JSON)
			api(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.12.1")
			api(group = "com.fasterxml.jackson.module", name = "jackson-module-parameter-names", version = "2.12.1")
			api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310", version = "2.12.1")

			// WebSocket
			api(group = "com.neovisionaries", name = "nv-websocket-client", version = "2.10")

			// Annotations
			api(group = "org.jetbrains", name = "annotations", version = "20.1.0")
		}

		// Apache Commons
		api(group = "org.apache.commons", name = "commons-lang3")
		api(group = "commons-io", name = "commons-io")

		// Logging
		api(group = "org.slf4j", name = "slf4j-api")
		testImplementation(group = "ch.qos.logback", name = "logback-classic")

		// Test
		testImplementation(platform(mapOf("group" to "org.junit", "name" to "junit-bom", "version" to "5.7.0")))
		testImplementation(group = "org.junit.jupiter", name = "junit-jupiter")
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
			archiveClassifier.set("shaded")
			manifest.inheritFrom(jar.manifest)

			configurations.filter { it.isCanBeResolved }.flatMap {
				it.files.filter { it.exists() }
					.flatMap { JarFile(it).use { it.entries().toList().filter { it.name.endsWith(".class") && it.name != "module-info.class" }.toSet() } }
					.map { it.name.substringBeforeLast('/').replace('/', '.') }
			}.forEach {
				relocate(it, "com.github.twitch4j.shaded.${archiveVersion.map { it.replace(".", "_") }.get()}.$it") {
					exclude("com.github.twitch4j")
				}
			}
		}

		test {
			useJUnitPlatform {
				includeTags("unittest")
				excludeTags("integration")
			}
		}
	}

	publishing {
		publications {
			create<MavenPublication>("main") {
				from(components["java"])
				artifactId = project.artifactId
				pom.default()
			}
		}
	}

	bintray {
		user = bintrayUser
		key = bintrayApiKey
		setPublications(*publishing.publications.filterIsInstance<MavenPublication>().map { it.name }.toTypedArray())
		dryRun = false
		publish = true
		override = false
		pkg.apply {
			userOrg = "Twitch4J"
			repo = "maven"
			name = "Twitch4J"
			websiteUrl = "https://twitch4j.github.io/"
			issueTrackerUrl = "https://github.com/twitch4j/twitch4j/issues"
			vcsUrl = "https://github.com/twitch4j/twitch4j.git"
			publicDownloadNumbers = true
			version.apply {
				name = "${project.version}"
				vcsTag = "v${project.version}"
				released = "${Date()}"
			}
		}
	}
}

tasks.register<Javadoc>("aggregateJavadoc") {
	group = JavaBasePlugin.DOCUMENTATION_GROUP
	options {
		title = "${rootProject.name} (v${rootProject.version})"
		windowTitle = "${rootProject.name} (v${rootProject.version})"
		encoding = "UTF-8"
	}

	source(subprojects.map { it.tasks.delombok.get() })
	classpath = files(subprojects.flatMap { it.sourceSets["main"].compileClasspath.files })

	setDestinationDir(file("${rootDir}/docs/static/javadoc"))
}
