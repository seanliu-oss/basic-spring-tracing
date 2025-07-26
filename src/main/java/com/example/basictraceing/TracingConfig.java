package com.example.basictraceing;

import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationConfig;
import brave.baggage.CorrelationScopeConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.B3Propagation;
import brave.propagation.CurrentTraceContext;
import brave.propagation.Propagation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public BaggageField traceIdField() {
        return BaggageField.create("trace_id");
    }

    @Bean
    public BaggageField correlationIdField() {
        return BaggageField.create("correlationId");
    }

    @Bean
    public BaggageField xTraceIdField() {
        return BaggageField.create("x-b3-traceid");
    }

    @Bean
    public CurrentTraceContext.ScopeDecorator mdcScopeDecorator() {
        return MDCScopeDecorator.newBuilder().add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(traceIdField()).flushOnUpdate().build()).add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(correlationIdField()).flushOnUpdate().build()).build();
    }

    @Bean
    public Propagation.Factory propagationFactory(BaggageField traceIdField, BaggageField correlationIdField) {
        return BaggagePropagation.newFactoryBuilder(B3Propagation.FACTORY).add(BaggagePropagationConfig.SingleBaggageField.remote(traceIdField)).add(BaggagePropagationConfig.SingleBaggageField.remote(correlationIdField)).build();
    }
}
