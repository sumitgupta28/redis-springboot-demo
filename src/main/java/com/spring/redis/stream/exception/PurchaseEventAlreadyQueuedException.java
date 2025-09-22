package com.spring.redis.stream.exception;


public class PurchaseEventAlreadyQueuedException extends RuntimeException {
    public PurchaseEventAlreadyQueuedException(String jobId) {
        super("job with id " + jobId + " is already queued");
    }
}
