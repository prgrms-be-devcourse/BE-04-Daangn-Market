package com.devcourse.be04daangnmarket.common.aop.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class DistributedLockAspect {
    private static final long WAIT_TIME = 30;
    private static final long LEASE_TIME = 10;
    private static final String KEY_PREFIX = "getPostLock_%s";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    public DistributedLockAspect(RedissonClient redissonClient, AopForTransaction aopForTransaction) {
        this.redissonClient = redissonClient;
        this.aopForTransaction = aopForTransaction;
    }

    @Around("@annotation(com.devcourse.be04daangnmarket.common.aop.lock.DistributedRock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String appointmentCode = String.valueOf(args[0]);

        RLock lock = redissonClient.getLock(String.format(KEY_PREFIX, appointmentCode));

        try {
            boolean isLocked = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (isLocked) {
                return aopForTransaction.proceed(joinPoint);
            } else {
                throw new LockAcquisitionFailureException(ErrorMessage.LOCK_ACQUISITION_FAILURE.getMessage());
            }
        } catch (InterruptedException e) {
            throw new LockAcquisitionFailureException(ErrorMessage.LOCK_DURING_ACQUISITION_FAILURE.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
