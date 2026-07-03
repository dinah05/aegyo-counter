# 멀티스테이지 빌드 (deployment.md 기준)
FROM gradle:9.3.1-jdk21-jammy AS build
WORKDIR /workspace
COPY . .
# 루트 멀티모듈 프로젝트에서 :server 만 빌드 (Android SDK 없이 :app 제외)
RUN gradle :server:bootJar -PbackendOnly -x test --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
ENV GRADLE_OPTS="-Xmx512m -Xms256m"
COPY --from=build /workspace/server/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
