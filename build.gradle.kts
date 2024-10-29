plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "io.github.agramar"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}


repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    flatDir {
        dirs("libs")
    }
}

extra["springAiVersion"] = "1.0.0-M3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.ai:spring-ai-ollama-spring-boot-starter")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

val profile = if (project.hasProperty("profile")) project.property("profile") else "local"

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources", "src/main/resources-env/$profile")
        }
    }
}


tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
    test {
        useJUnitPlatform()
    }
    jar {
        enabled = false
    }
    clean {
        delete("src/main/generated")
    }
}

