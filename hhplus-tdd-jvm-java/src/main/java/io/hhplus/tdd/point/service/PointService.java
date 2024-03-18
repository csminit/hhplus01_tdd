package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.exception.PointException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public UserPoint findPoint(Long id) {
        // validation
        if (id == null) {
            throw new PointException.IdNotExistException("유저 정보를 입력해 주세요.");
        }

        // 특정 유저의 포인트 조회
        return userPointTable.selectById(id);
    }

    public UserPoint charge(Long id, Long amount) {
        // validation - 충전하려는 포인트가 0원보다 커야 함
        if (amount == null || amount == 0) {
            throw new PointException.AmountNotExistException("충전할 포인트는 0원 이상이어야 합니다.");
        }

        // 포인트 충전
        UserPoint userPoint = userPointTable.insertOrUpdate(id, amount);
        // 포인트 충전 내역 추가
        pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return userPoint;
    }

    public UserPoint use(Long id, Long amount) {
        // validation - 사용하려는 포인트가 0원보다 커야 함
        if (amount == null || amount == 0) {
            throw new PointException.AmountNotExistException("사용할 포인트는 0원 이상이어야 합니다.");
        }

        // validation - 사용하려는 포인트가 잔액보다 클 수 없음
        UserPoint currentUserPoint = userPointTable.selectById(id);
        if (currentUserPoint.point() < amount) {
            throw new PointException.UseAmountExceedChargedException("잔고가 부족합니다.");
        }

        // 포인트 사용
        UserPoint remainUserPoint = userPointTable.insertOrUpdate(currentUserPoint.id(), currentUserPoint.point() - amount);
        // 포인트 이용 내역 추가
        pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

        return remainUserPoint;
    }

    public List<PointHistory> findHistoryList(Long id) {
        // validation
        if (id == null) {
            throw new PointException.IdNotExistException("유저 정보를 입력해 주세요.");
        }

        // 포인트 충전/이용 내역 조회
        return pointHistoryTable.selectAllByUserId(id);
    }
}
