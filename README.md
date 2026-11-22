# Async Audit Logging / 비동기 요청 로그

[ENG](#eng) | [KR](#kr)

## ENG

Uses @EnableAsync and @Async to write request metadata on a worker thread. A CompletableFuture returns the worker name or a failure.

### Project structure

```text
pom.xml
src/
├── main/
│   └── java/io/github/parkwoong/demo/
│       ├── DemoApplication.java
│       └── AuditService.java
└── test/java/io/github/parkwoong/demo/
    └── AuditServiceTest.java
```

Java package: `io.github.parkwoong.demo`.

| File | Role |
|---|---|
| [DemoApplication.java](src/main/java/io/github/parkwoong/demo/DemoApplication.java) | Starts Spring Boot and runs the example. |
| [AuditService.java](src/main/java/io/github/parkwoong/demo/AuditService.java) | Validates the HTTP method and status, writes a log, and returns the worker thread name. |
| [pom.xml](pom.xml) | Dependencies and build settings. |

### How it works

DemoApplication defines auditExecutor and calls the Spring-managed AuditService bean. record("GET", 200) runs on an audit- worker thread. The console runner waits with get() so the result is visible. The executor has 1 core thread, up to 2 threads, and a queue size of 100.

### Run

Requirements: Java 17+ and Maven 3.6.3+. Run these commands from the repository root.

```sh
mvn clean verify
mvn spring-boot:run
```

Look for method=GET status=200 and a worker name such as audit-1. Press Ctrl+C to stop the app after checking the output.

### Tests

AuditServiceTest checks that the call runs on an audit- thread and that an invalid method reaches the caller as a failed future.

[AuditServiceTest.java](src/test/java/io/github/parkwoong/demo/AuditServiceTest.java)

### Scope and limits

Only the method and status are logged; tokens, headers, bodies, and query strings are not logged. Logs go to the console, not a durable audit store. When the executor is full, submission can fail immediately. There is no HTTP endpoint.

This learning example uses Spring Boot 3.5.16. Before production use, move to a supported version; see the [3.5 OSS support notice](https://spring.io/blog/2026/06/25/spring-boot-3-5-16-available-now/). Production connections and load tests are not included.

Adapted from a Java proxy project using generic names and sample data. Original Git history, production settings, and business identifiers are not included.

## KR

@EnableAsync와 @Async로 요청 메타데이터를 작업 스레드에서 기록합니다. CompletableFuture를 통해 작업 스레드 이름 또는 실패를 전달합니다.

### 프로젝트 구조

```text
pom.xml
src/
├── main/
│   └── java/io/github/parkwoong/demo/
│       ├── DemoApplication.java
│       └── AuditService.java
└── test/java/io/github/parkwoong/demo/
    └── AuditServiceTest.java
```

Java 패키지: `io.github.parkwoong.demo`.

| 파일 | 역할 |
|---|---|
| [DemoApplication.java](src/main/java/io/github/parkwoong/demo/DemoApplication.java) | Spring Boot를 시작하고 예제를 실행합니다. |
| [AuditService.java](src/main/java/io/github/parkwoong/demo/AuditService.java) | HTTP 메서드와 상태 코드를 검증하고 로그를 남긴 뒤 작업 스레드 이름을 반환합니다. |
| [pom.xml](pom.xml) | 의존성과 빌드 설정입니다. |

### 동작 방식

DemoApplication이 auditExecutor를 등록하고 Spring이 관리하는 AuditService 빈을 호출합니다. record("GET", 200)은 audit- 이름의 작업 스레드에서 실행됩니다. 콘솔 실행기는 결과 확인을 위해 get()으로 기다립니다. 스레드 풀은 기본 1개, 최대 2개 스레드와 크기 100의 큐를 사용합니다.

### 실행 방법

Java 17 이상, Maven 3.6.3 이상이 필요합니다. 저장소 루트에서 실행하세요.

```sh
mvn clean verify
mvn spring-boot:run
```

method=GET status=200 로그와 audit-1 같은 작업 스레드 이름을 확인하세요. 출력 확인 후 Ctrl+C로 종료합니다.

### 테스트

AuditServiceTest는 audit- 작업 스레드 실행과 잘못된 메서드 입력 시 Future를 통한 실패 전달을 확인합니다.

[AuditServiceTest.java](src/test/java/io/github/parkwoong/demo/AuditServiceTest.java)

### 범위와 제한

메서드와 상태 코드만 기록하며 토큰·헤더·본문·쿼리는 기록하지 않습니다. 로그는 콘솔에 출력되며 영구 감사 저장소에 저장되지 않습니다. 실행기가 포화되면 작업 제출이 즉시 실패할 수 있습니다. HTTP 엔드포인트는 없습니다.

Spring Boot 3.5.16 기반 학습용 예제입니다. 운영 도입 전 지원 버전으로 전환하세요. [3.5 OSS 지원 종료 안내](https://spring.io/blog/2026/06/25/spring-boot-3-5-16-available-now/)를 참고할 수 있습니다. 운영 시스템 연결 및 부하 테스트는 포함하지 않습니다.

기존 Java 프록시 프로젝트에서 기술을 추출해 범용 이름과 샘플 데이터로 재작성했습니다. 기존 Git 이력, 운영 설정 및 업무 식별자는 포함하지 않습니다.
