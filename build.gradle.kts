import com.coditory.gradle.manifest.ManifestPluginExtension
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.freefair.gradle.plugins.lombok.LombokExtension
import io.freefair.gradle.plugins.lombok.tasks.Delombok
import me.champeau.jmh.JmhParameters

plugins {
	signing
	`java-library`
	`maven-publish`
	id("io.freefair.lombok").version("8.10.2").apply(false)
	id("com.coditory.manifest").version("0.2.6").apply(false)
	id("me.champeau.jmh").version("0.7.2").apply(false)
	id("com.gradleup.shadow").version("8.3.5").apply(false)
	id("com.github.gmazzo.buildconfig").version("5.3.5").apply(false)
}

group = group
version = version

allprojects {
	repositories {
		mavenCentral()
	}
}

/**
 * Enables com.coditory.manifest plugin for `publish` tasks or if `-PenableManifest` is supplied trough cli
 */
val enableManifest = with(project) {
	gradle.startParameter.taskNames.any { s -> s.startsWith("publish") }
			|| properties.containsKey("enableManifest")
}

// Subprojects
subprojects {
	apply(plugin = "signing")
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")
	apply(plugin = "io.freefair.lombok")
	apply(plugin = "me.champeau.jmh")

	if (enableManifest) {
		apply(plugin = "com.coditory.manifest")
		project.extensions
				.getByType(ManifestPluginExtension::class.java)
				.apply { buildAttributes = false }
	}

	project.extensions.getByType(LombokExtension::class).apply {
		version.set("1.18.34")
		disableConfig.set(true)
	}

	project.extensions.getByType(JmhParameters::class).apply {
		iterations.set(4)
		fork.set(1)
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
			api(group = "org.jetbrains", name = "annotations", version = "26.0.1")

			// Apache Commons
			api(group = "commons-configuration", name = "commons-configuration", version = "1.10")

			// Rate Limiting
			api(group = "com.bucket4j", name = "bucket4j_jdk8-core", version = "8.10.1")

			// HTTP
			api(group = "com.squareup.okhttp3", name = "okhttp", version = "4.12.0")

			// Credential Manager
			api(group = "com.github.philippheuer.credentialmanager", name = "credentialmanager", version = "0.3.1")

			// HTTP Client
			api(group = "io.github.openfeign", name = "feign-slf4j", version = "13.5")
			api(group = "io.github.openfeign", name = "feign-okhttp", version = "13.5")
			api(group = "io.github.openfeign", name = "feign-jackson", version = "13.5")
			api(group = "io.github.openfeign", name = "feign-hystrix", version = "13.5")

			// WebSocket
			api(group = "com.neovisionaries", name = "nv-websocket-client", version = "2.14")

			// Regex
			api(group = "com.github.tony19", name = "named-regexp", version = "1.0.0")

			// Hystrix
			api(group = "com.netflix.hystrix", name = "hystrix-core", version = "1.5.18")

			// rich version declarations
			listOf("com.fasterxml.jackson.core:jackson-annotations", "com.fasterxml.jackson.core:jackson-core", "com.fasterxml.jackson.core:jackson-databind", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310").forEach { dep ->
				add("api", dep) {
					version {
						strictly("[2.15,3-alpha[")
						// renovate: depName=com.fasterxml.jackson:jackson-bom
						prefer("2.18.1")
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

		// Apache Commons
		api(group = "commons-io", name = "commons-io", version = "2.17.0")
		api(group = "org.apache.commons", name = "commons-lang3", version = "3.17.0")

		// Cache BOM
		api(platform("io.github.xanthic.cache:cache-bom:0.6.2"))

		// Events4J BOM
		api(platform("com.github.philippheuer.events4j:events4j-bom:0.12.2"))

		// Logging
		api(group = "org.slf4j", name = "slf4j-api", version = "2.0.16")

		// Jackson BOM
		api(platform("com.fasterxml.jackson:jackson-bom:2.18.1"))

		// Test
		testImplementation(platform("org.junit:junit-bom:5.11.3"))
		testImplementation(group = "org.junit.jupiter", name = "junit-jupiter")
		// - Mocking
		testImplementation(platform("org.mockito:mockito-bom:5.14.2"))
		// - Await
		testImplementation(group = "org.awaitility", name = "awaitility", version = "4.2.2")
		// - Logging
		testImplementation(group = "ch.qos.logback", name = "logback-classic", version = "1.3.14")
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
		// jar artifact id and version
		withType<Jar> {
			if (this is ShadowJar) {
				archiveClassifier.set("shaded")
				isEnableRelocation = true
				relocationPrefix = "com.github.twitch4j.shaded"

				// support for multi-release jars since we depend upon jackson-core, which leverages FastDoubleParser
				dependencies {
					// https://github.com/johnrengelman/shadow/issues/729
					exclude("META-INF/versions/**/module-info.class")
				}
				manifest {
					// https://github.com/johnrengelman/shadow/issues/449
					attributes("Multi-Release" to true)
				}
			}
			if (enableManifest) {
				manifest.from(File(buildDir, "resources/main/META-INF/MANIFEST.MF"))
			}
		}

		// reproducible builds
		withType<AbstractArchiveTask>().configureEach {
			isPreserveFileTimestamps = false
			isReproducibleFileOrder = true
		}

		withType<Sign>().configureEach {
			onlyIf {
				publishToMavenLocal.run { !isPresent || !project.gradle.taskGraph.hasTask(this.get()) }
			}
		}

		// compile options
		withType<JavaCompile> {
			options.encoding = "UTF-8"
		}

		withType<Javadoc> {
			options {
				this as StandardJavadocDocletOptions
				links(
						"https://javadoc.io/doc/org.jetbrains/annotations/26.0.1",
						"https://javadoc.io/doc/commons-configuration/commons-configuration/1.10",
						"https://javadoc.io/doc/com.bucket4j/bucket4j_jdk8-core/8.10.1",
						// "https://javadoc.io/doc/com.squareup.okhttp3/okhttp/4.12.0", // blocked by https://github.com/square/okhttp/issues/6450
						"https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-core/0.12.2",
						"https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-handler-simple/0.12.2",
						"https://javadoc.io/doc/com.github.philippheuer.credentialmanager/credentialmanager/0.3.1",
						"https://javadoc.io/doc/io.github.openfeign/feign-slf4j/13.5",
						"https://javadoc.io/doc/io.github.openfeign/feign-okhttp/13.5",
						"https://javadoc.io/doc/io.github.openfeign/feign-jackson/13.5",
						"https://javadoc.io/doc/io.github.openfeign/feign-hystrix/13.5",
						"https://javadoc.io/doc/org.slf4j/slf4j-api/2.0.16",
						"https://javadoc.io/doc/com.neovisionaries/nv-websocket-client/2.14",
						"https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/2.18.1",
						"https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-core/2.18.1",
						"https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-annotations/2.18.1",
						"https://javadoc.io/doc/commons-io/commons-io/2.17.0",
						"https://javadoc.io/doc/org.apache.commons/commons-lang3/3.17.0",
						"https://javadoc.io/doc/org.projectlombok/lombok/1.18.34",
						"https://twitch4j.github.io/javadoc"
				)
				locale = "en"

				// additional javadoc tags
				tags = listOf(
					"apiNote:a:API Note:",
					"implSpec:a:Implementation Requirements:",
					"implNote:a:Implementation Note:"
				)
			}
		}

		// javadoc & delombok
		val delombok by getting(Delombok::class)
		javadoc {
			dependsOn(delombok)
			source(delombok)
			options {
				title = "${project.name} (v${project.version})"
				windowTitle = "${project.name} (v${project.version})"
				encoding = "UTF-8"
				overview = "../buildSrc/overview-single.html"
				this as StandardJavadocDocletOptions
				// hide javadoc warnings (a lot from delombok)
				addStringOption("Xdoclint:none", "-quiet")
				if (JavaVersion.current().isJava9Compatible) {
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
		addStringOption("Xdoclint:none", "-quiet")
		if (JavaVersion.current().isJava9Compatible) {
			addBooleanOption("html5", true)
		}
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
