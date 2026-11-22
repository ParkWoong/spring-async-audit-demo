package io.github.parkwoong.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
@EnableAsync
@SpringBootApplication
public class DemoApplication {
 public static void main(String[] args) { SpringApplication.run(DemoApplication.class, args); }
 @Bean("auditExecutor") ThreadPoolTaskExecutor executor() {
 var executor = new ThreadPoolTaskExecutor();
 executor.setCorePoolSize(1); executor.setMaxPoolSize(2); executor.setQueueCapacity(100);
 executor.setThreadNamePrefix("audit-"); executor.setWaitForTasksToCompleteOnShutdown(true); executor.setAwaitTerminationSeconds(5);
 return executor;
 }
 @Bean CommandLineRunner demo(AuditService audit) { return args -> { System.out.println(audit.record("GET", 200).get()); }; }
}
