package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final LockManager lockManager;
    private final PointValidator pointValidator;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserPointRepository userPointRepository;

    public UserPoint findPoint(Long id) {
        // validation
        pointValidator.idCheck(id);

        // 특정 유저의 포인트 조회
        return userPointRepository.selectById(id);
    }

    public UserPoint charge(Long id, Long amount) {
        return lockManager.executeWithLock(id, () -> {
            // validation - 충전하려는 포인트가 0원보다 커야 함
            pointValidator.amountNotExist(amount);

            // 포인트 충전
            UserPoint currentUserPoint = userPointRepository.selectById(id);
            if (currentUserPoint == null) {
                currentUserPoint = UserPoint.empty(id);
            }
            UserPoint userPoint = userPointRepository.insertOrUpdate(id, currentUserPoint.point() + amount);
            // 포인트 충전 내역 추가
            pointHistoryRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

            return userPoint;
        });
    }

    public UserPoint use(Long id, Long amount) {
        return lockManager.executeWithLock(id, () -> {
            // validation - 사용하려는 포인트가 0원보다 커야 함
            pointValidator.amountNotExist(amount);

            // validation - 사용하려는 포인트가 잔액보다 클 수 없음
            UserPoint currentUserPoint = userPointRepository.selectById(id);
            pointValidator.useAmountExceedChargedPoint(amount, currentUserPoint.point());

            // 포인트 사용
            UserPoint remainUserPoint = userPointRepository.insertOrUpdate(currentUserPoint.id(), currentUserPoint.point() - amount);
            // 포인트 이용 내역 추가
            pointHistoryRepository.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

            return remainUserPoint;
        });
    }

    public List<PointHistory> findHistoryList(Long id) {
        // validation
        pointValidator.idCheck(id);

        // 포인트 충전/이용 내역 조회
        return pointHistoryRepository.selectAllByUserId(id);
    }
}
