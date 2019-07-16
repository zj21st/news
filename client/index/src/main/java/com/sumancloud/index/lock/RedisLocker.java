package com.sumancloud.index.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLocker  implements DistributedLocker{

    private final static String LOCKER_PREFIX = "lock:";

    @Autowired
    RedissonClient redisson;

    @Override
    public <T> T lock(String resourceName, AquiredLockWorker<T> worker) throws InterruptedException, UnableToAquireLockException, Exception {

        return lock(resourceName, worker, 100);
    }

    @Override
    public <T> T lock(String resourceName, AquiredLockWorker<T> worker, int lockTime) throws UnableToAquireLockException, Exception {
        //RedissonClient redisson= redissonConnector.getClient();
        RLock mylock = redisson.getLock(LOCKER_PREFIX + resourceName);
        // Wait for 100 seconds seconds and automatically unlock it after lockTime seconds
        boolean success = mylock.tryLock(100, lockTime, TimeUnit.SECONDS);
        if (success) {
            System.out.println("-----------------get a locker----------------------");
            try {
                return worker.invokeAfterLockAquire();
            } finally {
                mylock.unlock();
            }
        }else{
            System.out.println("-----------------can not get locker----------------------");
        }
        throw new UnableToAquireLockException();
    }
}
