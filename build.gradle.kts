import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.3.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"

	id("com.commercehub.gradle.plugin.avro") version "0.19.1"
	id("com.commercehub.gradle.plugin.avro-base") version "0.19.1"
}

group = "wr"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven(url = "https://packages.confluent.io/maven/")
}

extra["confluentVersion"] = "5.2.1"
extra["springCloudVersion"] = "Hoxton.SR8"

dependencies {
	implementation("io.github.microutils:kotlin-logging:1.7.6")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams")
	implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")

	implementation("io.confluent:kafka-avro-serializer:${property("confluentVersion")}")
	implementation("io.confluent:kafka-streams-avro-serde:${property("confluentVersion")}")
	implementation("org.apache.avro:avro:1.9.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
