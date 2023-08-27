import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
	java
	id("org.springframework.boot") version "2.4.2"
	id("io.spring.dependency-management") version "1.1.2"
}

group = "com.countries"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok:1.18.28")
	annotationProcessor("org.projectlombok:lombok:1.18.28")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation(group = "io.springfox", name = "springfox-boot-starter", version = "3.0.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	useJUnitPlatform()

	testLogging {
		events("passed", "skipped", "failed", "standardOut", "standardError")
		exceptionFormat = TestExceptionFormat.FULL
		showCauses = true
		showExceptions = true
		showStackTraces = true
	}
}

