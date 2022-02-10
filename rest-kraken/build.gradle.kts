// In this section you declare the dependencies for your production and test code
dependencies {
	// HTTP Client
	api(group = "io.github.openfeign", name = "feign-okhttp")
	api(group = "io.github.openfeign", name = "feign-jackson")
	api(group = "io.github.openfeign", name = "feign-slf4j")
	api(group = "io.github.openfeign", name = "feign-hystrix")
	api(group = "commons-configuration", name = "commons-configuration")

	// Jackson (JSON)
	api(group = "com.fasterxml.jackson.core", name = "jackson-databind")
	api(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

	// Twitch4J Modules
	api(project(":twitch4j-common"))
	api(project(":twitch4j-auth"))
}

tasks.javadoc {
	options {
		title = "Twitch4J (v${version}) - Kraken Module API <sup>(deprecated)</sup>"
		windowTitle = "Twitch4J (v${version}) - Kraken Module API"
	}
}

publishing.publications.withType<MavenPublication> {
	pom {
		name.set("Twitch4J API - Kraken Module")
		description.set("Kraken API dependency")
	  // TODO: Uncomment after decommission + remove this comment + remove this module in the next release
//		distributionManagement {
//			relocation {
//				groupId.set("$group")
//				artifactId.set("twitch4j-helix")
//				message.set("Due endpoints is deprecated decommission have been planned on Febuary 28, 2022. More details about decommission finds here: https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline")
//			}
//		}
	}
}
