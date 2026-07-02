# Aegyo Counter — Backend (`:server`)

애교 카운터의 백엔드 모듈. [pp11t5/backend-core](https://github.com/pp11t5/backend-core) 팀 핸드북 기준으로 세팅했다.

## 스택

- Kotlin / Java 21 / Spring Boot 4 (webmvc, validation, actuator)
- **DB 없음** — 저장소는 인메모리(`ConcurrentHashMap`). 서버 재시작 시 데이터 초기화됨
- 공통 응답 `ApiResponse<T>`, `{도메인}{HTTP코드}{순번}` 에러코드, 전역 예외 처리
- SpringDoc(Swagger UI)

## 멀티모듈 구조 — "서버만 빌드"

이 저장소는 `:app`(Android) + `:server`(Spring Boot) 단일 Gradle 멀티모듈이다.
`:server`는 항상 포함되고, `:app`은 **Android SDK가 있을 때만** 자동 포함된다.

| 환경 | 동작 |
|---|---|
| Android 개발 머신 (SDK/`local.properties`/`ANDROID_HOME` 있음) | `:app` + `:server` 둘 다 |
| 백엔드 서버 / CI / Docker (SDK 없음) | **`:server`만** 자동 빌드 — 별도 플래그 불필요 |

- 강제로 서버만: `-PbackendOnly` (또는 `BACKEND_ONLY=true`)
- SDK가 있는데 자동감지 안 될 때 강제 포함: `-PwithAndroid` (또는 `WITH_ANDROID=true`)

즉, **이 레포를 그대로 백엔드에 연결하면 `:app` 없이 `:server`만 빌드된다.**

## 실행

```bash
# 로컬 실행 (기본 프로파일 local)
./gradlew :server:bootRun

# 테스트
./gradlew :server:test

# 빌드 (Android SDK 없는 곳이면 자동으로 server만)
./gradlew :server:bootJar
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- 헬스체크: http://localhost:8080/actuator/health

환경변수는 `.env.example` 참고.

## API

베이스: `/api/v1`. 모든 응답은 `ApiResponse<T>` 래퍼.

### Counter (멀티 유저)

| Method | Path | 설명 |
|---|---|---|
| GET | `/counters/{userKey}` | 카운터 조회 |
| POST | `/counters/{userKey}/increment` | +1. 임계값(기본 50) 돌파 시 Discord 알림 |
| POST | `/counters/{userKey}/decrement` | -1 (0 미만 불가) |
| POST | `/counters/{userKey}/reset` | 리셋 (비밀번호 필요) |
| GET | `/counters/ranking` | allBest 상위 10 랭킹 |

리셋 요청 바디:
```json
{ "password": "COUNTER_RESET_PASSWORD 값" }
```

### Issue

| Method | Path | 설명 |
|---|---|---|
| POST | `/issues` | 이슈 등록 |
| GET | `/issues/{id}` | 단건 조회 |
| GET | `/issues` | 목록 조회 |

등록 요청 바디:
```json
{ "title": "제목", "content": "내용(input)", "assignee": null }
```
- `assignee`를 비우면 **`codebidoof`** 로 고정 할당된다.
- `link`는 하드코딩(`https://github.com/LinkYou-2025/LinkU_Android/issues`)으로 자동 연결된다.

## Discord 웹훅 알림

`count`가 임계값(기본 50)을 **넘어서는 순간 1회** Discord 웹훅으로 메시지를 보낸다.

- `DISCORD_WEBHOOK_ENABLED=false`(로컬 기본): 실제 전송 대신 로그 출력(`LoggingNotificationSender`)
- `DISCORD_WEBHOOK_ENABLED=true` + `DISCORD_WEBHOOK_URL` 설정: 실제 전송(`DiscordWebhookSender`)

## 배포

`server/Dockerfile` 멀티스테이지 빌드(`-PbackendOnly`). Railway 등에 `SPRING_PROFILES_ACTIVE=prod` +
`COUNTER_RESET_PASSWORD`, `DISCORD_WEBHOOK_URL` 환경변수로 배포. (DB 없음 — 재시작 시 데이터 초기화)
