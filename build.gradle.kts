// Plugins
plugins {
	`java-library`
	id("me.philippheuer.configuration") version "+"
}

// root + subprojects
allprojects {
	apply(plugin = "java-library")
	apply(plugin = "me.philippheuer.configuration")

	repositories {
		mavenCentral()
	}

	projectConfiguration {
		logLevel.set(LogLevel.INFO)

		// config
		javadocLombok.set(false)
		javadocOverviewTemplate.set("${rootDir}/buildSrc/overview-single.html")
		javadocOverviewAggregateTemplate.set("${rootDir}/buildSrc/overview-general.html")
		javadocAutoLinking.set(true)
		javadocGroups.set(javadocPackageNames())
	}
}

// subprojects
subprojects {
	projectConfiguration {
		logLevel.set(LogLevel.INFO)

		// project
		language.set(me.philippheuer.projectcfg.domain.ProjectLanguage.JAVA)
		type.set(me.philippheuer.projectcfg.domain.ProjectType.LIBRARY)
		javaVersion.set(JavaVersion.VERSION_1_8)

		// pom
		pom = {pom -> pom.default()}

		// shading
		shadow.set(true)
		shadowRelocate.set("com.github.twitch4j.shaded")
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
			api(group = "com.github.vladimir-bukhtoyarov", name = "bucket4j-core", version = "7.0.0")

			// HTTP
			api(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.3")

			// Event Dispatcher
			api(group = "com.github.philippheuer.events4j", name = "events4j-core", version = "0.9.8")
			api(group = "com.github.philippheuer.events4j", name = "events4j-handler-simple", version = "0.9.8")

			// Credential Manager
			api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager", version = "0.1.2")

			// HTTP Client
			api(group = "io.github.openfeign", name = "feign-slf4j", version = "11.7")
			api(group = "io.github.openfeign", name = "feign-okhttp", version = "11.7")
			api(group = "io.github.openfeign", name = "feign-jackson", version = "11.7")
			api(group = "io.github.openfeign", name = "feign-hystrix", version = "11.7")

			// WebSocket
			api(group = "com.neovisionaries", name = "nv-websocket-client", version = "2.14")

			// Hystrix
			api(group = "com.netflix.hystrix", name = "hystrix-core", version = "1.5.18")

			// rich version declarations
			listOf("com.fasterxml.jackson.core:jackson-annotations", "com.fasterxml.jackson.core:jackson-core", "com.fasterxml.jackson.core:jackson-databind", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310").forEach { dep ->
				add("api", dep) {
					version {
						strictly("[2.12,3[")
						prefer("2.13.1")
					}
				}
			}
		}

		// Apache Commons
		api(group = "commons-io", name = "commons-io", version = "2.11.0")
		api(group = "org.apache.commons", name = "commons-lang3", version = "3.12.0")

		// Logging
		api(group = "org.slf4j", name = "slf4j-api", version = "1.7.32")

		// Jackson BOM
		api(platform("com.fasterxml.jackson:jackson-bom:2.13.1"))

		// Test
		testImplementation(platform("org.junit:junit-bom:5.8.2"))
		testImplementation(group = "org.junit.jupiter", name = "junit-jupiter")
		testImplementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.9")
	}
}
