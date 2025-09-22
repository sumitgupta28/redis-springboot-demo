package com.spring.redis.stream.exception;


public class RemovingRunningPurchaseEventException extends RuntimeException {
    public RemovingRunningPurchaseEventException(String jobId) {
        super("job with id " + jobId + " is running and can't be removed from the queue");
    }
}
