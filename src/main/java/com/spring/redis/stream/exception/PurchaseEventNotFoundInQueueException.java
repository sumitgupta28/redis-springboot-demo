package com.spring.redis.stream.exception;


public class PurchaseEventNotFoundInQueueException extends RuntimeException {
    public PurchaseEventNotFoundInQueueException(String jobId) {
        super("job with id " + jobId + " is not queued");
    }
}
