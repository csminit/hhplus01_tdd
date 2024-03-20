package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.exception.PointCustomException;
import org.springframework.stereotype.Component;

@Component
public class PointValidator {

    // id 값 존재 체크
    void idCheck(Long id) {
        if (id == null) {
            throw new PointCustomException("유저 정보를 입력해 주세요.");
        }
    }

    // 사용 또는 충전하려는 포인트가 0원보다 커야 함
    void amountNotExist(Long amount) {
        if (amount == null || amount == 0) {
            throw new PointCustomException("충전할 포인트는 0원 이상이어야 합니다.");
        }
    }

    // 사용하려는 포인트가 잔액보다 클 수 없음
    void useAmountExceedChargedPoint(Long amount, Long currentPoint) {
        if (currentPoint < amount) {
            throw new PointCustomException("잔고가 부족합니다.");
        }
    }
}
