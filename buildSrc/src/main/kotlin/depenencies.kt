import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec

// Jackson
val DependencyHandler.JACKSON_BOM
	get() = "com.fasterxml.jackson:jackson-bom:2.12.1"
val DependencyHandler.JACKSON_DATABIND
	get() = "com.fasterxml.jackson.core:jackson-databind"
val DependencyHandler.JACKSON_DATATYPE_JSR310
	get() = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"

// JUnit
val DependencyHandler.JUNIT_BOM
	get() = "org.junit:junit-bom:5.7.0"
val DependencyHandler.JUNIT_JUPITER
	get() = "org.junit.jupiter:junit-jupiter"

// Rate Limiting
val DependencyHandler.BUCKET4J
	get() = "com.github.vladimir-bukhtoyarov:bucket4j-core:4.7.0"

// Caching
val DependencyHandler.CAFFEINE
	get() = "com.github.ben-manes.caffeine:caffeine:2.8.6"

// Annotations
val DependencyHandler.ANNOTATIONS
	get() = "org.jetbrains:annotations:20.1.0"

// Logging
val DependencyHandler.SLF4J
	get() = "org.slf4j:slf4j-api:1.7.30"
val DependencyHandler.LOGBACK
	get() = "ch.qos.logback:logback-classic:1.2.3"

// Apache Commons
val DependencyHandler.COMMONS_IO
	get() = "commons-io:commons-io:2.8.0"
val DependencyHandler.COMMONS_LANG3
	get() = "org.apache.commons:commons-lang3:3.11"
val DependencyHandler.COMMONS_CONFIGURATION
	get() = "commons-configuration:commons-configuration:1.10"

// HTTP Client
val DependencyHandler.OKHTTP3
	get() = "com.squareup.okhttp3:okhttp:4.9.0"

// Event Dispatcher
val DependencyHandler.EVENTS_CORE
	get() = "com.github.philippheuer.events4j:events4j-core:0.9.5"
val DependencyHandler.EVENTS_SIMPLE
	get() = "com.github.philippheuer.events4j:events4j-handler-simple:0.9.5"
val DependencyHandler.EVENTS_REACTOR
	get() = "com.github.philippheuer.events4j:events4j-handler-simple:0.9.5"
val DependencyHandler.COMMONS_SPRING
	get() = "com.github.philippheuer.events4j:events4j-handler-simple:0.9.5"

// Credential Manager
val DependencyHandler.CREDENTIAL_MANAGER
	get() = "com.github.philippheuer.credentialmanager:credentialmanager:0.1.1"

// Credential Manager
val DependencyHandler.CREDENTIAL_MANAGER_EWS
	get() = "com.github.philippheuer.credentialmanager:credentialmanager-ews:0.1.1"

// Http Client
val DependencyHandler.FEIGN_OKHTTP
	get() = "io.github.openfeign:feign-okhttp:11.0"
val DependencyHandler.FEIGN_JACKSON
	get() = "io.github.openfeign:feign-jackson:11.0"
val DependencyHandler.FEIGN_SLF4J
	get() = "io.github.openfeign:feign-slf4j:11.0"
val DependencyHandler.FEIGN_HYSTRIX
	get() = "io.github.openfeign:feign-hystrix:11.0"

// Hystrix
val DependencyHandler.HYSTRIX
	get() = "com.netflix.hystrix:hystrix-core:1.5.18"

// WebSocket
val DependencyHandler.NV_WEBSOCKET
	get() = "com.neovisionaries:nv-websocket-client:2.10"

// Apollo
val DependencyHandler.APOLLO_RUNTIME
	get() = "com.apollographql.apollo:apollo-runtime:2.5.2"

val PluginDependenciesSpec.apollo
	get() = id("com.apollographql.apollo") version "2.5.2"
