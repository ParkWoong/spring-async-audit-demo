package io.github.parkwoong.demo;

import io.github.parkwoong.demo.infra.mapper.ApiRequestLogMapper;
import io.github.parkwoong.demo.service.LogService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class AuditController {
    private final LogService logService;
    private final ApiRequestLogMapper mapper;

    public AuditController(LogService logService, ApiRequestLogMapper mapper) {
        this.logService = logService;
        this.mapper = mapper;
    }

    @PostMapping("/api/audit")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> record(@RequestBody Map<String, String> input) {
        logService.logRequest(
                input.get("userId"),
                input.get("clientId"),
                "sample-key",
                "{}",
                "{}",
                input.get("requestUri"),
                input.get("responseCode"));
        return Collections.singletonMap("status", "accepted");
    }

    @GetMapping("/api/audit/count")
    public Map<String, Integer> count() {
        return Collections.singletonMap("count", mapper.countRequestHistory());
    }
}
