import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import java.util.*

fun MavenPom.default() {
	url.set("https://twitch4j.github.io/")
	issueManagement {
		url.set("https://github.com/twitch4j/twitch4j/issues")
	}
	ciManagement {
		url.set("https://github.com/twitch4j/twitch4j/actions")
	}
	inceptionYear.set(Calendar.getInstance().get(Calendar.YEAR).toString())
	developers { all }
	licenses {
		license {
			name.set("MIT Licence")
			distribution.set("repo")
			url.set("https://github.com/twitch4j/twitch4j/blob/master/LICENSE")
		}
	}
	scm {
		connection.set("scm:git:https://github.com/twitch4j/twitch4j.git")
		developerConnection.set("scm:git:git@github.com:twitch4j/twitch4j.git")
		url.set("https://github.com/twitch4j/twitch4j")
	}
	distributionManagement {
		downloadUrl.set("https://bintray.com/twitch4j/maven/twitch4j/_latestVersion")
	}
}

val MavenPomDeveloperSpec.all: Unit
	get() {
		PhilippHeuer()
	}

fun MavenPomDeveloperSpec.PhilippHeuer() {
	developer {
		id.set("PhilippHeuer")
		name.set("Philipp Heuer")
		email.set("git@philippheuer.me")
		roles.addAll("creator", "developer", "owner")
	}
}
