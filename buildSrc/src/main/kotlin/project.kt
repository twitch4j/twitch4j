import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomDeveloperSpec

fun MavenPom.default() {
	url.set("https://twitch4j.github.io")
	issueManagement {
		system.set("GitHub")
		url.set("https://github.com/twitch4j/twitch4j/issues")
	}
	inceptionYear.set("2017")
	developers {
		developer {
			id.set("PhilippHeuer")
			name.set("Philipp Heuer")
			email.set("git@philippheuer.me")
			roles.addAll("maintainer")
		}
		developer {
			id.set("iProdigy")
			name.set("Sidd")
			roles.addAll("maintainer")
		}
	}
	licenses {
		license {
			name.set("MIT Licence")
			distribution.set("repo")
			url.set("https://opensource.org/licenses/MIT")
		}
	}
	scm {
		connection.set("scm:git:https://github.com/twitch4j/twitch4j.git")
		developerConnection.set("scm:git:git@github.com:twitch4j/twitch4j.git")
		url.set("https://github.com/twitch4j/twitch4j")
	}
}

fun javadocPackageNames(): Map<String, String> {
	return mapOf(
		"com.github.twitch4j" to "Core",
		"com.github.twitch4j.domain*" to "Core",
		"com.github.twitch4j.events*" to "Core",
		"com.github.twitch4j.modules*" to "Core",
		"com.github.twitch4j.common*" to "Common",
		"com.github.twitch4j.auth*" to "Auth",
		"com.github.twitch4j.chat*" to "Chat",
		"com.github.twitch4j.helix*" to "Helix API",
		"com.github.twitch4j.eventsub*" to "EventSub",
		"com.github.twitch4j.pubsub*" to "PubSub",
		"com.github.twitch4j.tmi*" to "Twitch Message Interface - API",
		"com.github.twitch4j.graphql*" to "GraphQL",
		"com.github.twitch4j.extensions*" to "Extensions API (deprecated)",
		"com.github.twitch4j.kraken*" to "Kraken API v5 (deprecated)",
	)
}
