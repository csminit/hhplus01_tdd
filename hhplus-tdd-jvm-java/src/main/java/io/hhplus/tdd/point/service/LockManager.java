package io.hhplus.tdd.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LockManager {
    /*락 객체들을 정기적으로 정리하는 클래스*/

    // 유저 ID와 락 객체를 매핑
    private final ConcurrentHashMap<Long, Lock> userLocks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Long> accessTime = new ConcurrentHashMap<>();

    public LockManager() {
        cleanupLock();
    }

    // 동일한 유저의 호출에 대해 동시성 제어
    protected Lock getUserLock(Long id) {
        accessTime.put(id, System.currentTimeMillis());
        return userLocks.computeIfAbsent(id, lock -> new ReentrantLock());
    }

    private void cleanupLock() {
        Thread cleanupThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    userLocks.keySet().removeIf(id -> {
                        Long lastAccessTime = accessTime.get(id);
                        // 1분 동안 사용되지 않은 락 제거
                        return lastAccessTime != null && (System.currentTimeMillis() - lastAccessTime) > 60_000;
                    });
                    // 10초마다 확인
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
