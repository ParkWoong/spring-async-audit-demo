package io.github.parkwoong.demo.service;

import io.github.parkwoong.demo.infra.entity.RequestLog;
import io.github.parkwoong.demo.infra.repository.ApiRequestLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Value("${logging.request}")
    private boolean loggingRequest;

    @Autowired private ApiRequestLogRepository logRepository;

    @Async
    public void logRequest(
            String userId,
            String clientId,
            String tokenKeyId,
            String requestHeader,
            String requestBody,
            String requestUri,
            String responseCode) {

        if (loggingRequest)
            try {

                final RequestLog log =
                        RequestLog.builder()
                                .userId(userId)
                                .clientId(clientId)
                                .tokenKeyId(tokenKeyId)
                                .requestHeader(requestHeader)
                                .requestBody(requestBody)
                                .requestUri(requestUri)
                                .responseCode(responseCode)
                                .build();

                logRepository.saveRequestHistory(log);

            } catch (Exception e) {

                e.printStackTrace();
            }
    }
}
