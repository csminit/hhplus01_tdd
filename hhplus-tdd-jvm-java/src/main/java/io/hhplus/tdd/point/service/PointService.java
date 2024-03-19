package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.exception.PointCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    // 유저 ID와 락 객체를 매핑
    private final ConcurrentHashMap<Long, Lock> userLocks = new ConcurrentHashMap<>();
    // 동일한 유저의 호출에 대해 동시성 제어
    private Lock getUserLock(Long id) {
        return userLocks.computeIfAbsent(id, k -> new ReentrantLock());
    }

    public UserPoint findPoint(Long id) {
        // validation
        if (id == null) {
            throw new PointCustomException("유저 정보를 입력해 주세요.");
        }

        // 특정 유저의 포인트 조회
        return userPointTable.selectById(id);
    }

    public UserPoint charge(Long id, Long amount) {
        Lock lock = getUserLock(id);
        lock.lock();
        try {
            // validation - 충전하려는 포인트가 0원보다 커야 함
            if (amount == null || amount == 0) {
                throw new PointCustomException("충전할 포인트는 0원 이상이어야 합니다.");
            }

            // 포인트 충전
            UserPoint userPoint = userPointTable.insertOrUpdate(id, amount);
            // 포인트 충전 내역 추가
            pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

            return userPoint;

        } finally {
            lock.unlock();
        }
    }

    public UserPoint use(Long id, Long amount) {
        Lock lock = getUserLock(id);
        lock.lock();
        try {
            // validation - 사용하려는 포인트가 0원보다 커야 함
            if (amount == null || amount == 0) {
                throw new PointCustomException("사용할 포인트는 0원 이상이어야 합니다.");
            }

            // validation - 사용하려는 포인트가 잔액보다 클 수 없음
            UserPoint currentUserPoint = userPointTable.selectById(id);
            if (currentUserPoint.point() < amount) {
                throw new PointCustomException("잔고가 부족합니다.");
            }

            // 포인트 사용
            UserPoint remainUserPoint = userPointTable.insertOrUpdate(currentUserPoint.id(), currentUserPoint.point() - amount);
            // 포인트 이용 내역 추가
            pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

            return remainUserPoint;

        } finally {
            lock.unlock();
        }
    }

    public List<PointHistory> findHistoryList(Long id) {
        // validation
        if (id == null) {
            throw new PointCustomException("유저 정보를 입력해 주세요.");
        }

        // 포인트 충전/이용 내역 조회
        return pointHistoryTable.selectAllByUserId(id);
    }
}
