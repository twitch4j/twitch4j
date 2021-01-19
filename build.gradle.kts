@file:Suppress("UnstableApiUsage")

import java.util.*

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

	// Source Compatibility
	java {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
		withSourcesJar()
		withJavadocJar()
	}

	// Dependency Management for Subprojects
	dependencies {
		// Apache Commons
		api(COMMONS_IO)
		api(COMMONS_LANG3)

		// Logging
		api(SLF4J)

		// Jackson BOM
		implementation(platform(JACKSON_BOM))

		// Test
		testImplementation(platform(JUNIT_BOM))
		testImplementation(JUNIT_JUPITER)
		testImplementation(LOGBACK)
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
		title = "${rootProject.name} (v${project.version})"
		windowTitle = "${rootProject.name} (v${project.version})"
		encoding = "UTF-8"
	}

	source(subprojects.map { it.tasks.delombok.get() })
	classpath = files(subprojects.map { it.sourceSets["main"].compileClasspath })

	setDestinationDir(file("${rootDir}/docs/static/javadoc"))
}
