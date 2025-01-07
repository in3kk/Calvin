plugins {
	java
	war
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "calvin"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.google.code.gson:gson:2.8.6") // gson 추가
	implementation("commons-io:commons-io:2.6") // commons-io 추가
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	annotationProcessor("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	implementation ("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.withType<JavaCompile>{
	options.encoding = "UTF-8"
}