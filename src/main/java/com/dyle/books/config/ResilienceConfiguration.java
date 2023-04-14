package com.dyle.books.config;

import com.dyle.books.commons.resilience.ResilienceEventsLoggingHandler;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sasa Lukic
 */
@Configuration
public class ResilienceConfiguration {

    public static final String DATA_ACCESS_RETRY_NAME = "data-access";

    @Bean
    ResilienceEventsLoggingHandler resilienceEventsLoggingHandler(RetryRegistry retryRegistry,
                                                                  CircuitBreakerRegistry circuitBreakerRegistry,
                                                                  RateLimiterRegistry rateLimiterRegistry,
                                                                  BulkheadRegistry bulkheadRegistry) {
        return new ResilienceEventsLoggingHandler(retryRegistry, circuitBreakerRegistry, rateLimiterRegistry, bulkheadRegistry);
    }
}
