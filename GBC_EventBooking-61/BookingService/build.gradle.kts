plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(22)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://packages.confluent.io/maven/")
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.1.2")

	implementation("org.springframework.kafka:spring-kafka:3.3.0")
	testImplementation("org.springframework.kafka:spring-kafka-test:3.3.0")
	testImplementation("org.testcontainers:kafka:1.20.4")

	implementation("org.apache.avro:avro:1.12.0")
	implementation("io.confluent:kafka-avro-serializer:7.5.0")
	implementation("io.confluent:kafka-schema-registry-client:7.7.1")
	implementation(project(":shared-schema"))

	testImplementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")

	compileOnly("org.projectlombok:lombok")
//	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mongodb")
	testImplementation("io.rest-assured:rest-assured")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
