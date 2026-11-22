package io.github.parkwoong.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.parkwoong.demo.service.LogService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class LogServiceTest {
    @Autowired private LogService service;
    @Autowired private JdbcTemplate jdbc;

    @Test
    void originalAsyncServiceWritesToDatabase() throws InterruptedException {
        service.logRequest(
                "sample-user", "sample-client", "sample-key", "[]", "sample", "/sample", "200");
        long deadline = System.nanoTime() + java.util.concurrent.TimeUnit.SECONDS.toNanos(5);
        while (jdbc.queryForObject("select count(*) from api_request_log", Integer.class) == 0
                && System.nanoTime() < deadline) {
            Thread.sleep(25);
        }
        assertEquals(1, jdbc.queryForObject("select count(*) from api_request_log", Integer.class));
        assertEquals(
                "sample",
                jdbc.queryForObject("select request_body from api_request_log", String.class));
    }
}
