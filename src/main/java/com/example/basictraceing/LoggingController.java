package com.example.basictraceing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoggingController {

    private static final Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @GetMapping("/test-logging")
    public String testLogging(@RequestHeader Map<String, String> headers) {
        logger.info("test-logging is called");
        logger.info("Trace ID: {}", MDC.get("traceId"));
        logger.info("Trace ID: {}", MDC.get("trace_id"));
        logger.info("Correlation ID: {}", MDC.get("correlationId"));
        logger.info("Span ID: {}", MDC.get("spanId"));
        logger.info("Headers: {}", headers);
        return "test-logging is called";
    }
}
