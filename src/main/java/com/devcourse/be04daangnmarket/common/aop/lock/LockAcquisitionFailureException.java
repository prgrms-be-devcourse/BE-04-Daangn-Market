package com.devcourse.be04daangnmarket.common.aop.lock;

public class LockAcquisitionFailureException extends RuntimeException {
    public LockAcquisitionFailureException(String message) {
        super(message);
    }
}
