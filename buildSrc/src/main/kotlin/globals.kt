import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject
import org.gradle.util.Path

val Project.bintrayUser: String?
	get() = System.getenv("BINTRAY_USER") ?: findProperty("bintray.user")?.toString()

val Project.bintrayApiKey: String?
	get() = System.getenv("BINTRAY_API_KEY") ?: findProperty("bintray.api_key")?.toString()

val Project.githubToken: String?
	get() = System.getenv("GITHUB_TOKEN") ?: findProperty("github.token")?.toString()

val Project.isSnapshot: Boolean
	get() = (rootProject.version as String).endsWith("-SNAPSHOT")

val Project.artifactId: String
	get() = (this as DefaultProject).identityPath.path.replace(Path.SEPARATOR, "-").let {
		if (rootProject.name.equals(it.substring(1), true)) rootProject.name else (rootProject.name + if (it.startsWith("-") && it.length > 1) it else "")
	}.toLowerCase()

enum class VersionType(private val pattern: Regex) {
	MAJOR(Regex("(\\d+).\\d+.\\d+-.*")),
	MINOR(Regex("\\d+.(\\d+).\\d+-.*")),
	PATCH(Regex("\\d+.\\d+.(\\d+)-.*"));

	internal fun replace(version: String) = version.replace(pattern, "${"$1".toInt() + 1}")
}

fun nextIterable(version: String, type: VersionType, snapshot: Boolean = true) =
	type.replace(version) + (if (snapshot) "-SNAPSHOT" else "")
