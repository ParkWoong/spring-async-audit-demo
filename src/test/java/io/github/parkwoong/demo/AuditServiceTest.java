package io.github.parkwoong.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AuditServiceTest {
 @Autowired AuditService audit;
 @Test void runsOnWorkerAndPropagatesFailure() throws Exception {
  assertTrue(audit.record("POST", 201).get(5, TimeUnit.SECONDS).startsWith("audit-"));
  var failure = assertThrows(ExecutionException.class, () -> audit.record("GET\nforged", 200).get(5, TimeUnit.SECONDS));
  assertInstanceOf(IllegalArgumentException.class, failure.getCause());
 }
}
