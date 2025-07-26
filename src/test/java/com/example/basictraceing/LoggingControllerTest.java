package com.example.basictraceing;

import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation;
import brave.propagation.B3Propagation;
import brave.propagation.TraceContextOrSamplingFlags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
public class LoggingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BaggageField traceIdField;

    @Autowired
    private BaggageField correlationIdField;

    @Test
    public void testLoggingEndpoint(CapturedOutput output) {
        String traceId = "test-trace-id";
        String correlationId = "test-correlation-id";

        HttpHeaders headers = new HttpHeaders();
        headers.set(traceIdField.name(), traceId);
        headers.set(correlationIdField.name(), correlationId);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = this.restTemplate.exchange("http://localhost:" + port + "/test-logging", HttpMethod.GET, entity, String.class);
        assertThat(response.getBody()).isEqualTo("test-logging is called");

        assertThat(output).contains("trace_id: " + traceId, "correlationId: " + correlationId);
    }
}
