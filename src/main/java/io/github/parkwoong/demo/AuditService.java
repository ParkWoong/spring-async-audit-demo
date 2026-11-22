package io.github.parkwoong.demo;

import java.util.concurrent.CompletableFuture;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class AuditService {
 @Async("auditExecutor")
 public CompletableFuture<String> record(String method, int status) {
  if (method == null || !method.matches("[A-Z]{3,10}") || status < 100 || status > 599)
   throw new IllegalArgumentException("Invalid audit event");
  LoggerFactory.getLogger(AuditService.class).info("method={} status={}", method, status);
  return CompletableFuture.completedFuture(Thread.currentThread().getName());
 }
}
