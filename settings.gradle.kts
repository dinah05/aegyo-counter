pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AegyoCounter"

// :server(Spring Boot)는 항상 포함한다.
// :app(Android)은 "Android SDK가 있는 환경"에서만 포함한다.
// -> 이 레포를 백엔드 서버/CI/Docker(=Android SDK 없음)에 연결하면 자동으로 :server 만 빌드된다.
//    (Gradle은 include된 모든 모듈을 configure 하는데, :app configure에는 Android SDK가 필요하기 때문)
//
// 강제 제외: -PbackendOnly 또는 BACKEND_ONLY=true
// 강제 포함: -PwithAndroid 또는 WITH_ANDROID=true (SDK가 있는데 자동감지 실패할 때)
val backendOnly = providers.gradleProperty("backendOnly").isPresent ||
    System.getenv("BACKEND_ONLY") == "true"
val forceAndroid = providers.gradleProperty("withAndroid").isPresent ||
    System.getenv("WITH_ANDROID") == "true"

val androidSdkAvailable = file("local.properties").exists() ||
    System.getenv("ANDROID_HOME") != null ||
    System.getenv("ANDROID_SDK_ROOT") != null

if (!backendOnly && (forceAndroid || androidSdkAvailable)) {
    include(":app")
}
include(":server")
