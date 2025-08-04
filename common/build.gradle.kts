import com.github.gmazzo.buildconfig.BuildConfigExtension

plugins {
	id("com.github.gmazzo.buildconfig")
}

dependencies {
	// Event Manager
	api(libs.events4j.core)
	api(libs.events4j.handler.simple)

	// HTTP Client (for common feign extensions/interceptors/...)
	compileOnly(libs.feign.core)
	compileOnly(libs.feign.okhttp)
	compileOnly(libs.feign.jackson)
	compileOnly(libs.feign.slf4j)
	compileOnly(libs.feign.hystrix)

	// Jackson (JSON)
	api(libs.jackson.datatype.jsr310)

	// Rate-limit buckets for registry
	compileOnly(libs.bucket4j.core)

	// Twitch4J Modules
	api(project(":twitch4j-auth"))
	api(project(":twitch4j-util"))
}

projectConfiguration {
	artifactDisplayName.set("Twitch4J API - Common Module")
	artifactDescription.set("Common API dependency")
	javadocTitle.set("Twitch4J (v${version}) - Common Module API")
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
