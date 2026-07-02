import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    // kotlin.jvm 플러그인은 Android/Compose 모듈을 통해 이미 classpath에 올라와 있으므로
    // 버전을 지정하지 않고 classpath 버전(2.2.10)을 그대로 사용한다.
    kotlin("jvm")
    // allopen(spring) 플러그인은 classpath에 없으므로 catalog 버전으로 적용한다.
    alias(libs.plugins.kotlin.spring)
}

group = "com.aegyocounter"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
        // 팀 핸드북(tech-stack.md) 기준 컴파일러 옵션
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xannotation-default-target=param-property",
        )
    }
}

dependencies {
    // Web / Validation / Actuator
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // API 문서 (SpringDoc OpenAPI / Swagger UI)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${libs.versions.springdoc.get()}")

    // 저장소는 인메모리(ConcurrentHashMap) — DB 없음

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
