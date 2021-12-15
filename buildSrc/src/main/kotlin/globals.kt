import org.gradle.api.Project

val Project.mavenRepositoryUrl: String
	get() = System.getenv("MAVEN_REPO_URL") ?: findProperty("maven.repository.url").toString()

val Project.mavenRepositoryUsername: String
	get() = System.getenv("MAVEN_REPO_USERNAME") ?: findProperty("maven.repository.username").toString()

val Project.mavenRepositoryPassword: String
	get() = System.getenv("MAVEN_REPO_PASSWORD") ?: findProperty("maven.repository.password").toString()

