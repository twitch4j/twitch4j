import com.github.gmazzo.buildconfig.BuildConfigExtension

plugins {
	id("com.github.gmazzo.buildconfig")
}

dependencies {
	// Event Manager
	api(group = "com.github.philippheuer.events4j", name = "events4j-core")
	api(group = "com.github.philippheuer.events4j", name = "events4j-handler-simple")

	// HTTP Client (for common feign extensions/interceptors/...)
	compileOnly(group = "io.github.openfeign", name = "feign-okhttp")
	compileOnly(group = "io.github.openfeign", name = "feign-jackson")
	compileOnly(group = "io.github.openfeign", name = "feign-slf4j")
	compileOnly(group = "io.github.openfeign", name = "feign-hystrix")

	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Rate-limit buckets for registry
	compileOnly(group = "com.bucket4j", name = "bucket4j_jdk8-core")

	// Twitch4J Modules
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
