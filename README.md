# Async Request Logging / 비동기 요청 로그

[ENG](#eng) | [KR](#kr)

## ENG

### Project structure

```text
pom.xml
src/main/java/io/github/parkwoong/demo/DemoApplication.java
src/main/java/io/github/parkwoong/demo/AuditController.java
src/main/java/io/github/parkwoong/demo/config/AsyncConfig.java
src/main/java/io/github/parkwoong/demo/infra/entity/RequestLog.java
src/main/java/io/github/parkwoong/demo/infra/mapper/ApiRequestLogMapper.java
src/main/java/io/github/parkwoong/demo/infra/repository/ApiRequestLogRepository.java
src/main/java/io/github/parkwoong/demo/service/LogService.java
src/main/java/io/github/parkwoong/demo/spec/util/LogUtils.java
src/main/resources/application.properties
src/main/resources/schema.sql
src/test/java/io/github/parkwoong/demo/LogServiceTest.java
```

### Code explanation

`AsyncConfig` enables `@Async`. A Spring-managed caller invokes `LogService.logRequest`; when `logging.request=true`, the service builds a `RequestLog` and calls `ApiRequestLogRepository`. The repository adds a timestamp/random ID with `LogUtils`, then `ApiRequestLogMapper` inserts the row.

The service stores the header/body strings passed to it. Use sanitized sample data. Exceptions are printed inside the asynchronous method. `LogUtils` uses a shared `SimpleDateFormat` and a 0–99 suffix, so it is not a thread-safe unique-ID generator.

AuditController submits a sample audit request to LogService and reads the H2 row count.

### Run and test

Use JDK 8 and Maven 3.6.3+. Set JAVA_HOME to your JDK 8 directory, then run from the repository root:

```sh
java -version
mvn -version
mvn clean verify
mvn spring-boot:run
```

`LogServiceTest` calls the asynchronous bean and waits for the row to appear in H2.

The app listens on localhost:8080. The original unit tests still check the underlying methods.

#### Postman API

Send POST http://localhost:8080/api/audit with Content-Type: application/json and body {"userId":"sample","clientId":"sample-client","requestUri":"/sample","responseCode":"200"}. Expected: HTTP 202, {"status":"accepted"}. Then call GET http://localhost:8080/api/audit/count. Expected: HTTP 200, {"count":1} after the asynchronous insert finishes. H2 data resets when the app stops.

## KR

### 프로젝트 구조

```text
pom.xml
src/main/java/io/github/parkwoong/demo/DemoApplication.java
src/main/java/io/github/parkwoong/demo/AuditController.java
src/main/java/io/github/parkwoong/demo/config/AsyncConfig.java
src/main/java/io/github/parkwoong/demo/infra/entity/RequestLog.java
src/main/java/io/github/parkwoong/demo/infra/mapper/ApiRequestLogMapper.java
src/main/java/io/github/parkwoong/demo/infra/repository/ApiRequestLogRepository.java
src/main/java/io/github/parkwoong/demo/service/LogService.java
src/main/java/io/github/parkwoong/demo/spec/util/LogUtils.java
src/main/resources/application.properties
src/main/resources/schema.sql
src/test/java/io/github/parkwoong/demo/LogServiceTest.java
```

### 코드 설명

`AsyncConfig`가 `@Async`를 활성화합니다. Spring 빈을 통해 `LogService.logRequest`를 호출하면 `logging.request=true`일 때 `RequestLog`를 만들고 `ApiRequestLogRepository`에 전달합니다. Repository는 `LogUtils`로 시각·랜덤 ID를 추가하고 `ApiRequestLogMapper`로 행을 저장합니다.

전달받은 헤더·본문 문자열을 저장하므로 정제된 샘플 데이터를 사용하세요. 예외는 비동기 메서드 내부에서 출력합니다. `LogUtils`는 공유 `SimpleDateFormat`과 0~99 접미사를 사용하므로 스레드 안전한 고유 ID 생성기가 아닙니다.

AuditController는 샘플 기록을 LogService에 전달하고 H2의 저장 건수를 조회합니다.

### 실행과 테스트

JDK 8과 Maven 3.6.3 이상을 사용합니다. JAVA_HOME을 JDK 8 경로로 설정하고 저장소 루트에서 실행하세요:

```sh
java -version
mvn -version
mvn clean verify
mvn spring-boot:run
```

`LogServiceTest`는 비동기 빈을 호출하고 H2에 저장되는 결과를 확인합니다.

앱은 localhost:8080에서 실행됩니다. 기존 단위 테스트는 내부 메서드를 계속 확인합니다.

#### Postman API

Content-Type: application/json으로 POST http://localhost:8080/api/audit에 {"userId":"sample","clientId":"sample-client","requestUri":"/sample","responseCode":"200"}을 보내세요. 예상 결과: HTTP 202, {"status":"accepted"}. 이어서 GET http://localhost:8080/api/audit/count를 호출하면 비동기 저장 후 HTTP 200, {"count":1}이 됩니다. 앱 종료 시 H2 데이터는 사라집니다.
