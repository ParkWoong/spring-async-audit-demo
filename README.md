# Async Request Logging / 비동기 요청 로그

[ENG](#eng) | [KR](#kr)

## ENG

Keeps the original AsyncConfig → LogService → Repository → Mapper flow. @Async writes the original RequestLog fields to the database.

### Source and minimal changes

Kept the original void @Async method, field injection, builder, SQL insert, and caught exceptions. Renamed business-specific class/table prefixes. Added an H2 table and integration test. Removed the generated AuditService, custom executor, and CompletableFuture API.

| Original source | Extracted file |
|---|---|
| `ProxyApplication.java` | [DemoApplication.java](src/main/java/io/github/parkwoong/demo/DemoApplication.java) |
| `infra/entity/RequestLog.java` | [infra/entity/RequestLog.java](src/main/java/io/github/parkwoong/demo/infra/entity/RequestLog.java) |
| `infra/mapper/GfepApiRequestLogMapper.java` | [infra/mapper/ApiRequestLogMapper.java](src/main/java/io/github/parkwoong/demo/infra/mapper/ApiRequestLogMapper.java) |
| `infra/repository/GfepApiRequestLogRepository.java` | [infra/repository/ApiRequestLogRepository.java](src/main/java/io/github/parkwoong/demo/infra/repository/ApiRequestLogRepository.java) |
| `spec/util/LogUtils.java` | [spec/util/LogUtils.java](src/main/java/io/github/parkwoong/demo/spec/util/LogUtils.java) |
| `config/AsyncConfig.java` | [config/AsyncConfig.java](src/main/java/io/github/parkwoong/demo/config/AsyncConfig.java) |
| `service/LogService.java` | [service/LogService.java](src/main/java/io/github/parkwoong/demo/service/LogService.java) |

### Structure

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

### How it works

Call the Spring-managed LogService.logRequest bean. When logging.request=true, it builds RequestLog and calls saveRequestHistory. The repository adds a timestamp/random ID and the mapper inserts the row. LogServiceTest calls the real async bean and waits for the H2 row.

### Run and test

Use Java 17 and Maven 3.6.3+. Run from the repository root:

```sh
mvn clean verify
mvn spring-boot:run
```

The app starts without an HTTP server or an added console workflow. Use the tests under src/test for actual calls and results. Database modules initialize H2 at startup.

### Formatting

All Java files use google-java-format 1.24.0 with the AOSP style: four-space indentation, consistent line breaks, and organized imports.

### Original limits

Original behavior stores the header/body strings passed by the caller. Pass only synthetic, sanitized data in this demo. Exceptions are printed inside the async method, not returned to callers. LogUtils retains shared SimpleDateFormat and a 0–99 random suffix; it is not a thread-safe unique-ID generator.

The build keeps Spring Boot 2.6.6 and the original MyBatis/JJWT APIs for source fidelity. Lombok 1.18.36 supports the local Java 17 build. These are legacy dependencies; this extraction is for learning, not a production-ready upgrade. No production integration or load test is included.

## KR

원본 AsyncConfig → LogService → Repository → Mapper 흐름을 유지합니다. @Async로 원본 RequestLog 필드를 DB에 저장합니다.

### 원본과 최소 변경 사항

원본 void @Async 메서드, 필드 주입, builder, SQL INSERT 및 예외 처리 방식을 유지했습니다. 업무 전용 클래스·테이블 접두사를 변경하고 H2 테이블과 통합 테스트를 추가했습니다. 새로 작성했던 AuditService, 전용 실행기 및 CompletableFuture API는 제거했습니다.

| 원본 파일 | 추출 파일 |
|---|---|
| `ProxyApplication.java` | [DemoApplication.java](src/main/java/io/github/parkwoong/demo/DemoApplication.java) |
| `infra/entity/RequestLog.java` | [infra/entity/RequestLog.java](src/main/java/io/github/parkwoong/demo/infra/entity/RequestLog.java) |
| `infra/mapper/GfepApiRequestLogMapper.java` | [infra/mapper/ApiRequestLogMapper.java](src/main/java/io/github/parkwoong/demo/infra/mapper/ApiRequestLogMapper.java) |
| `infra/repository/GfepApiRequestLogRepository.java` | [infra/repository/ApiRequestLogRepository.java](src/main/java/io/github/parkwoong/demo/infra/repository/ApiRequestLogRepository.java) |
| `spec/util/LogUtils.java` | [spec/util/LogUtils.java](src/main/java/io/github/parkwoong/demo/spec/util/LogUtils.java) |
| `config/AsyncConfig.java` | [config/AsyncConfig.java](src/main/java/io/github/parkwoong/demo/config/AsyncConfig.java) |
| `service/LogService.java` | [service/LogService.java](src/main/java/io/github/parkwoong/demo/service/LogService.java) |

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

Spring이 관리하는 LogService.logRequest 빈을 호출합니다. logging.request=true이면 RequestLog를 만들고 saveRequestHistory를 호출합니다. Repository가 시각·랜덤 ID를 추가하고 Mapper가 저장합니다. LogServiceTest는 실제 비동기 빈을 호출하고 H2 저장 결과를 기다립니다.

### 실행과 테스트

Java 17, Maven 3.6.3 이상을 사용합니다. 저장소 루트에서 실행하세요:

```sh
mvn clean verify
mvn spring-boot:run
```

앱은 HTTP 서버나 별도 콘솔 시나리오 없이 시작합니다. 실제 호출과 결과 확인은 src/test의 테스트를 이용하세요. DB 모듈은 시작 시 H2를 초기화합니다.

### 코드 포맷

전체 Java 파일에 google-java-format 1.24.0의 AOSP 스타일을 적용했습니다. 들여쓰기 4칸, 일관된 줄바꿈 및 import 정리를 사용합니다.

### 유지한 원본 동작과 제한

원본처럼 호출자가 넘긴 헤더·본문 문자열을 저장합니다. 예제에는 정제한 가상 데이터만 전달하세요. 예외는 비동기 메서드 내부에서 출력하며 호출자에게 반환하지 않습니다. LogUtils의 공유 SimpleDateFormat과 0~99 랜덤 접미사를 유지했으므로 스레드 안전한 고유 ID 생성기가 아닙니다.

원본 코드 보존을 위해 Spring Boot 2.6.6과 원본 MyBatis/JJWT API를 사용합니다. 로컬 Java 17 빌드를 위해 Lombok 1.18.36을 적용했습니다. 레거시 의존성을 사용하는 학습용 추출본이며 운영용 업그레이드가 아닙니다. 운영 연동 및 부하 테스트는 포함하지 않습니다.
