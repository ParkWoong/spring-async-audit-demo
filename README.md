# Async Request Logging / 비동기 요청 로그

[ENG](#eng) | [KR](#kr)

## ENG

### Project structure

```text
pom.xml
src/main/java/io/github/parkwoong/demo/DemoApplication.java
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

### Run and test

Use JDK 8 and Maven 3.6.3+. Set JAVA_HOME to your JDK 8 directory, then run from the repository root:

```sh
java -version
mvn -version
mvn clean verify
mvn spring-boot:run
```

`LogServiceTest` calls the asynchronous bean and waits for the row to appear in H2.

The app starts without an HTTP server and exits. The test class shows the actual method calls and results. Database examples use H2 and need no external server.

## KR

### 프로젝트 구조

```text
pom.xml
src/main/java/io/github/parkwoong/demo/DemoApplication.java
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

### 실행과 테스트

JDK 8과 Maven 3.6.3 이상을 사용합니다. JAVA_HOME을 JDK 8 경로로 설정하고 저장소 루트에서 실행하세요:

```sh
java -version
mvn -version
mvn clean verify
mvn spring-boot:run
```

`LogServiceTest`는 비동기 빈을 호출하고 H2에 저장되는 결과를 확인합니다.

앱은 HTTP 서버 없이 시작 후 종료합니다. 실제 메서드 호출과 결과는 테스트 클래스에서 확인합니다. DB 예제는 H2를 사용하며 외부 서버는 필요하지 않습니다.
