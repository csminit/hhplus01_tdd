package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.exception.PointCustomException;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class PointServiceTest {

    @Mock
    private LockManager lockManager;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @Mock
    private UserPointRepository userPointRepository;

    private PointValidator pointValidator;
    private PointService pointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        pointValidator = new PointValidator();
        pointService = new PointService(lockManager, pointValidator, pointHistoryRepository, userPointRepository);
    }

    @Test
    @DisplayName("유저 값이 없으면 유저 포인트 조회에 실패한다")
    void failIfUserNotExists() {
        // when - then
        assertThrows(PointCustomException.class, () -> {
            pointValidator.idCheck(null);
        });
    }

    @Test
    @DisplayName("특정 유저의 포인트가 있으면 유저 포인트 정보를 리턴한다")
    void returnUserPointInfoWhenUserExists() {
        // given
        long id = 1L;
        UserPoint userPoint = new UserPoint(id, 100L, 0L);

        // when
        when(userPointRepository.selectById(id)).thenReturn(userPoint);
        UserPoint result = pointService.findPoint(id);

        // then
        assertNotNull(result);
        assertEquals(userPoint, result);
    }

    @Test
    @DisplayName("충전하려는 포인트가 0원보다 크지 않으면 충전에 실패한다")
    void failToChargeIfAmountIsNotGreaterThanZero() {
        // given
        long chargeAmount = 0L;

        // when - then
        assertThrows(PointCustomException.class, () -> {
            pointValidator.amountNotExist(chargeAmount);
        });
    }

    @Test
    @DisplayName("정상적인 유저 ID와 0원 이상의 충전할 포인트를 입력하면 충전된다")
    void chargePointWhenUserExistAndAmountIsGreaterThanZero() {
        // given
        long id = 1L;
        long chargeAmount = 1000L;
        UserPoint updateUserPoint = new UserPoint(id, 1000L, 0L);

        // when
        when(lockManager.executeWithLock(anyLong(), any())).thenReturn(updateUserPoint);
        when(userPointRepository.insertOrUpdate(id, chargeAmount)).thenReturn(updateUserPoint);
        UserPoint result = pointService.charge(id, chargeAmount);

        // then
        assertNotNull(result);
        assertEquals(result.id(), id);
        assertEquals(result.point(), chargeAmount);
    }

    @Test
    @DisplayName("유저가 포인트를 충전하면 포인트 충전 내역이 추가된다")
    void addPointHistoryWhenChargeUserPoint() {
        long id = 1L;
        long chargeAmount = 2000L;
        UserPoint currentUserPoint = new UserPoint(1L, 2000L, 0L);

        // when
        when(lockManager.executeWithLock(anyLong(), any())).thenReturn(currentUserPoint);
        when(userPointRepository.insertOrUpdate(id, chargeAmount)).thenReturn(currentUserPoint);
        pointService.charge(id, chargeAmount);
    }

    @Test
    @DisplayName("사용하려는 포인트가 0원보다 크지 않으면 사용에 실패한다")
    void failToUseIfUseAmountIsNotGreaterThanZero() {
        // given
        Long useAmount = 0L;

        // when - then
        assertThrows(PointCustomException.class, () -> pointValidator.amountNotExist(useAmount));
    }

    @Test
    @DisplayName("사용하려는 포인트가 현재 잔액보다 크면 사용에 실패한다")
    void failToUseIfUseAmountIsNotGreaterThanCurrentUserPoint() {
        // given
        long id = 1L;
        long useAmount = 2000L;
        UserPoint currentUserPoint = new UserPoint(1L, 1000L, 0L);

        // when
        when(userPointRepository.selectById(id)).thenReturn(currentUserPoint);

        // then
        assertThrows(PointCustomException.class, () -> pointValidator.useAmountExceedChargedPoint(useAmount, currentUserPoint.point()));
    }

    @Test
    @DisplayName("유저 잔액 내의 포인트를 사용한다")
    void useWhenGivenUserIdAndAmount() {
        // given
        long id = 1L;
        long useAmount = 200L;
        UserPoint currentUserPoint = new UserPoint(1L, 1000L, 0L);
        UserPoint remainUserPoint = new UserPoint(1L, 800L, System.currentTimeMillis());

        // when
        when(lockManager.executeWithLock(anyLong(), any())).thenReturn(remainUserPoint);
        when(userPointRepository.selectById(id)).thenReturn(currentUserPoint);
        when(userPointRepository.insertOrUpdate(currentUserPoint.id(), currentUserPoint.point() - useAmount)).thenReturn(remainUserPoint);
        UserPoint result = pointService.use(id, useAmount);

        // then
        assertNotNull(result);
        assertEquals(result.point(), 800L);
    }

    @Test
    @DisplayName("유저가 포인트를 사용하면 포인트 이용 내역을 추가한다")
    void addPointHistoryWhenUserUsePoint() {
        long id = 1L;
        long useAmount = 200L;
        UserPoint currentUserPoint = new UserPoint(1L, 1000L, 0L);
        UserPoint remainUserPoint = new UserPoint(1L, 800L, System.currentTimeMillis());

        // when
        when(lockManager.executeWithLock(anyLong(), any())).thenReturn(remainUserPoint);
        when(userPointRepository.selectById(id)).thenReturn(currentUserPoint);
        when(userPointRepository.insertOrUpdate(currentUserPoint.id(), currentUserPoint.point() - useAmount)).thenReturn(remainUserPoint);
        pointService.use(id, useAmount);
    }

    @Test
    @DisplayName("유저의 포인트 이용 내역을 조회한다")
    void getPointHistoryWhenUserExists() {
        // given
        long id = 1L;
        List<PointHistory> pointHistoryList = List.of(
                new PointHistory(1L, 1L, 1000L, TransactionType.CHARGE, 0L),
                new PointHistory(2L, 1L, 300L, TransactionType.USE, 1L)
        );

        // when
        when(pointHistoryRepository.selectAllByUserId(id)).thenReturn(pointHistoryList);
        List<PointHistory> result = pointService.findHistoryList(id);

        // then
        assertNotNull(result);
        assertEquals(result.size(), 2);
    }
}
