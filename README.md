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

`AsyncConfig` enables `@Async`. A Spring-managed caller invokes `LogService.logRequest`; when
`logging.request=true`, the service builds a `RequestLog` and calls `ApiRequestLogRepository`.
The repository adds a timestamp/random ID with `LogUtils`, then `ApiRequestLogMapper` inserts
the row.

The service stores the header/body strings passed to it. Use sanitized sample data. Exceptions
are printed inside the asynchronous method. `LogUtils` uses a shared `SimpleDateFormat` and a
0–99 suffix, so it is not a thread-safe unique-ID generator.

AuditController submits a sample audit request to LogService and reads the H2 row count.

### Run and test

Use JDK 8 and Maven 3.6.3+. Set JAVA_HOME to your JDK 8 directory, then run from the repository
root:

```sh
java -version
mvn -version
mvn clean verify
mvn spring-boot:run
```

`LogServiceTest` calls the asynchronous bean and waits for the row to appear in H2.

The app listens on localhost:8080. Unit tests cover the helper methods.

#### Postman API

##### 1. Write an audit entry

**Request**

- Method: POST
- URL: http://localhost:8080/api/audit
- Content-Type: application/json

~~~json
{
  "userId": "sample",
  "clientId": "sample-client",
  "requestUri": "/sample",
  "responseCode": "200"
}
~~~

**Response**

- Status: 202 Accepted

~~~json
{
  "status": "accepted"
}
~~~

##### 2. Check the saved row count

**Request**

- Method: GET
- URL: http://localhost:8080/api/audit/count
- Body: none

**Response**

- Status: 200 OK

~~~json
{
  "count": 1
}
~~~

**Note:** The insert is asynchronous. Retry after a moment if count is 0.

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

`AsyncConfig`가 `@Async`를 활성화한다. Spring 빈을 통해
`LogService.logRequest`를 호출하면 `logging.request=true` 일 때
`RequestLog`를 만들고 `ApiRequestLogRepository`에 전달한다. Repository는
`LogUtils`로 시각·랜덤 ID를 추가하고 `ApiRequestLogMapper`로 행을 저장한다.

전달받은 헤더·본문 문자열을 저장하므로 정제된 샘플 데이터를 사용. 예외는 비동기 메서드 내부에서 출력한다.
`LogUtils`는 공유 `SimpleDateFormat`과 0~99 접미사를 사용하므로 스레드 안전한 고유 ID
생성기가 아닙니다.

AuditController는 샘플 기록을 LogService에 전달하고 H2의 저장 건수를 조회한다.

### 실행과 테스트

JDK 8, Maven 3.6.3 이상 사용. 저장소 루트에서 실행.

```sh
java -version
mvn -version
mvn clean verify
mvn spring-boot:run
```

`LogServiceTest`는 비동기 빈을 호출하고 H2에 저장되는 결과를 확인한다.

앱은 localhost:8080에서 실행된다. 단위 테스트는 내부 메서드를 확인한다.

#### Postman API

##### 1. 감사 기록 저장

**요청**

- 메서드: POST
- URL: http://localhost:8080/api/audit
- Content-Type: application/json

~~~json
{
  "userId": "sample",
  "clientId": "sample-client",
  "requestUri": "/sample",
  "responseCode": "200"
}
~~~

**응답**

- 상태: 202 Accepted

~~~json
{
  "status": "accepted"
}
~~~

##### 2. 저장 건수 확인

**요청**

- 메서드: GET
- URL: http://localhost:8080/api/audit/count
- 본문: 없음

**응답**

- 상태: 200 OK

~~~json
{
  "count": 1
}
~~~

**참고:** 비동기 저장. count가 0이면 잠시 후 재호출.
