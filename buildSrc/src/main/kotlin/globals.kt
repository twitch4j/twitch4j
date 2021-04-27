import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject
import org.gradle.util.Path

val Project.mavenRepositoryUrl: String
	get() = System.getenv("MAVEN_REPO_URL") ?: findProperty("maven.repository.url").toString()

val Project.mavenRepositoryUsername: String
	get() = System.getenv("MAVEN_REPO_USERNAME") ?: findProperty("maven.repository.username").toString()

val Project.mavenRepositoryPassword: String
	get() = System.getenv("MAVEN_REPO_PASSWORD") ?: findProperty("maven.repository.password").toString()

val Project.artifactId: String
	get() = (this as DefaultProject).identityPath.path.replace(Path.SEPARATOR, "-").let {
		if (rootProject.name.equals(it.substring(1), true)) rootProject.name
		else (rootProject.name + if (it.startsWith("-") && it.length > 1) it else "")
	}.toLowerCase()
