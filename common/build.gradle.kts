import com.github.gmazzo.gradle.plugins.BuildConfigExtension

plugins {
	id("com.github.gmazzo.buildconfig")
}

dependencies {
	// Event Manager
	api("com.github.philippheuer.events4j:events4j-core")
	api("com.github.philippheuer.events4j:events4j-handler-simple")

	// Jackson (JSON)
	api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

	// HTTP Client (for common feign extensions/interceptors/...)
	compileOnly("io.github.openfeign:feign-core")

	// Rate-limit buckets for registry
	compileOnly("com.bucket4j:bucket4j_jdk8-core")

	// Twitch4J Modules
	api(project(":twitch4j-api"))
	api(project(":twitch4j-auth"))
	api(project(":twitch4j-util"))
}

project.extensions.getByType(BuildConfigExtension::class.java).apply {
	packageName("com.github.twitch4j.common.config")
	className("Twitch4JBuildConstants")

	useJavaOutput {
		defaultVisibility = true // this removes the public modifier from the generated class
	}

	forClass(packageName = "com.github.twitch4j.common.config", className = "Twitch4JBuildConstants") {
		buildConfigField("String", "VERSION", provider { "\"${project.version}\"" })
	}
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Common Module API"
		windowTitle = "Twitch4J (v${version}) - Common Module API"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Common Module")
		description.set("Common API dependency")
	}
}
