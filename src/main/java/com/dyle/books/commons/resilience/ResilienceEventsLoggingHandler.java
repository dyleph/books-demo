package com.dyle.books.commons.resilience;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.event.BulkheadEvent;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.event.RateLimiterEvent;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.event.RetryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sasa Lukic
 */
public class ResilienceEventsLoggingHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResilienceEventsLoggingHandler.class);

    public ResilienceEventsLoggingHandler(RetryRegistry retryRegistry,
                                          CircuitBreakerRegistry circuitBreakerRegistry,
                                          RateLimiterRegistry rateLimiterRegistry,
                                          BulkheadRegistry bulkheadRegistry) {

        retryRegistry.getAllRetries().forEach(r -> {
            logger.info("Registering event handler for the {} retry instance...", r.getName());
            r.getEventPublisher().onEvent(this::handleRetryEvent);
            logger.info("Retry event handler successfully registered");
        });

        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            logger.info("Registering event handler for the {} circuit breaker instance...", cb.getName());
            cb.getEventPublisher().onEvent(this::handleCircuitBreakerEvent);
            logger.info("Circuit breaker vent handler successfully registered");
        });

        rateLimiterRegistry.getAllRateLimiters().forEach(rl -> {
            logger.info("Registering event handler for the {} rate limiter instance...", rl.getName());
            rl.getEventPublisher().onEvent(this::handleRateLimiterEvent);
            logger.info("Rate limiter event handler successfully registered");
        });

        bulkheadRegistry.getAllBulkheads().forEach(b -> {
            logger.info("Registering event handler for the {} bulkhead instance...", b.getName());
            b.getEventPublisher().onEvent(this::handleBulkheadEvent);
            logger.info("Bulkhead event handler successfully registered");
        });
    }

    private void handleRetryEvent(RetryEvent e) {
        logger.debug("Handling RetryEvent {}...", e.getName());

        switch (e.getEventType()) {
            case RETRY -> logger.warn("Unsuccessful attempt of a retryable operation (event: {})", e, e.getLastThrowable());
            case ERROR -> logger.error("Failed execution of a retryable operation (event: {})", e, e.getLastThrowable());
            case SUCCESS -> logger.info("Successful execution of a retryable operation (event: {})", e, e.getLastThrowable());
            case IGNORED_ERROR -> logger.debug("Ignored error for a retryable operation (event: {})", e, e.getLastThrowable());
            default -> logger.warn("Unhandled retry event type {} (event: {})", e.getEventType(), e, e.getLastThrowable());
        }
    }

    private void handleCircuitBreakerEvent(CircuitBreakerEvent e) {
        logger.debug("Handling CircuitBreakerEvent {}...", e.getCircuitBreakerName());

        switch (e.getEventType()) {
            case ERROR -> logger.warn("Unsuccessful attempt of a circuit breaker protected operation (event: {})", e);
            case IGNORED_ERROR -> logger.debug("Ignored error for a circuit breaker protected operation  (event: {})", e);
            case SUCCESS -> logger.debug("Successful execution of a circuit breaker protected operation (event: {})", e);
            case NOT_PERMITTED -> logger.warn("Not permitted execution of a circuit breaker protected operation (event: {})", e);
            case STATE_TRANSITION -> logger.warn("State transition for a circuit breaker of an operation (event: {})", e);
            case RESET -> logger.info("Reset circuit breaker for an operation (event: {})", e);
            case FORCED_OPEN -> logger.info("Forced open circuit breaker for an operation (event: {})", e);
            case DISABLED -> logger.info("Disabled circuit breaker for an operation (event: {})", e);
            default -> logger.warn("Unhandled circuit breaker event type {} (event: {})", e.getEventType(), e);
        }
    }

    private void handleRateLimiterEvent(RateLimiterEvent e) {
        logger.debug("Handling RateLimiterEvent {}...", e.getRateLimiterName());

        switch (e.getEventType()) {
            case FAILED_ACQUIRE -> logger.warn("Unsuccessful attempt of acquiring a permit for a rate limiter protected operation (event: {})", e);
            case SUCCESSFUL_ACQUIRE -> logger.debug("Successful acquisition of a permit for a rate limiter protected operation (event: {})", e);
            default -> logger.warn("Unhandled rate limiter event type {} (event: {})", e.getEventType(), e);
        }
    }

    private void handleBulkheadEvent(BulkheadEvent e) {
        logger.debug("Handling BulkheadEvent {}...", e.getBulkheadName());

        switch (e.getEventType()) {
            case CALL_PERMITTED -> logger.debug("Execution permitted for a bulkhead protected operation (event: {})", e);
            case CALL_REJECTED -> logger.warn("Execution rejected for a bulkhead protected operation (event: {})", e);
            case CALL_FINISHED -> logger.debug("Execution finished for a bulkhead protected operation (event: {})", e);
            default -> logger.warn("Unhandled bulkhead event type {} (event: {})", e.getEventType(), e);
        }
    }
}
