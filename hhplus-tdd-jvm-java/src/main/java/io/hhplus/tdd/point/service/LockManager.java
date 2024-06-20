package io.hhplus.tdd.point.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Service
public class LockManager {
    private final ConcurrentHashMap<Long, Lock> lockMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Long> accessTimeMap = new ConcurrentHashMap<>();

    public LockManager() {
        startCleanupThread();
    }

    public <T> T executeWithLock(Long key, Supplier<T> block) {
        Lock lock = getLock(key);
        boolean acquired = false;
        try {
            acquired = lock.tryLock();
            if (!acquired) {
                throw new RuntimeException("Unable to acquire lock");
            }
            return block.get();
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
    }

    private Lock getLock(Long key) {
        accessTimeMap.put(key, System.currentTimeMillis());
        return lockMap.computeIfAbsent(key, k -> new ReentrantLock());
    }

    private void startCleanupThread() {
        Thread cleanupThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    lockMap.keySet().removeIf(key -> {
                        Long lastAccessTime = accessTimeMap.get(key);
                        // 1분 동안 사용되지 않은 락 제거
                        return lastAccessTime != null && (System.currentTimeMillis() - lastAccessTime) > 60_000;
                    });
                    Thread.sleep(10_000); // 10초마다 확인
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "LockCleanupThread");
        cleanupThread.start();
    }
}
