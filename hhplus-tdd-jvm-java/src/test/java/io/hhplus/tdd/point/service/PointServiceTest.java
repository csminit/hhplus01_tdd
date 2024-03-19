package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.exception.PointCustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PointServiceTest {

    @Mock
    private UserPointTable userPointTable;
    @Mock
    private PointHistoryTable pointHistoryTable;

    private PointService pointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        pointService = new PointService(userPointTable, pointHistoryTable);
    }

    @Test
    @DisplayName("유저_값이_없으면_유저_포인트_조회_실패")
    void findPointTest_유저_값이_없으면_유저_포인트_조회_실패() {
        // when - then
        assertThrows(PointCustomException.class, () -> {
            pointService.findPoint(null);
        });
    }

    @Test
    @DisplayName("특정_유저의_포인트가_있으면_유저_포인트_정보를_리턴")
    void findPointTest_특정_유저의_포인트가_있으면_유저_포인트_정보를_리턴() {
        // given
        Long id = 1L;
        UserPoint userPoint = new UserPoint(id, 100L, 0L);

        // when
        when(userPointTable.selectById(id)).thenReturn(userPoint);
        UserPoint result = pointService.findPoint(id);

        // then
        assertNotNull(result);
        assertEquals(userPoint, result);
    }

    @Test
    @DisplayName("충전하려는_포인트가_0원보다_크지_않으면_충전_실패")
    void chargeTest_충전하려는_포인트가_0원보다_크지_않으면_충전_실패() {
        // given
        Long id = 1L;
        Long chargeAmount = 0L;

        // when - then
        assertThrows(PointCustomException.class, () -> {
            pointService.charge(id, chargeAmount);
        });
    }

    @Test
    @DisplayName("유저_ID와_충전할_포인트를_입력하면_포인트_충전")
    void chargeTest_유저_ID와_충전할_포인트를_입력하면_포인트_충전() {
        // given
        Long id = 1L;
        Long chargeAmount = 1000L;
        UserPoint updateUserPoint = new UserPoint(id, 1000L, 0L);

        // when
        when(userPointTable.insertOrUpdate(id, chargeAmount)).thenReturn(updateUserPoint);
        UserPoint result = pointService.charge(id, chargeAmount);

        // then
        assertNotNull(result);
        assertEquals(result.id(), 1L);
        assertEquals(result.point(), 1000L);
    }

    @Test
    @DisplayName("유저가_포인트_충전하면_포인트_충전_내역_추가")
    void chargeTest_유저가_포인트_충전하면_포인트_충전_내역_추가() {
        Long id = 1L;
        Long chargeAmount = 2000L;
        UserPoint currentUserPoint = new UserPoint(1L, 2000L, 0L);

        // when
        when(userPointTable.insertOrUpdate(id, chargeAmount)).thenReturn(currentUserPoint);
        pointService.charge(id, chargeAmount);

        // then
        verify(pointHistoryTable).insert(anyLong(), anyLong(), any(), anyLong());
    }

    @Test
    @DisplayName("사용하려는_포인트가_0원보다_크지_않으면_사용_실패")
    void useTest_사용하려는_포인트가_0원보다_크지_않으면_사용_실패() {
        // given
        Long id = 1L;
        Long useAmount = 0L;

        // when - then
        assertThrows(PointCustomException.class, () -> {
            pointService.use(id, useAmount);
        });
    }

    @Test
    @DisplayName("사용하려는_포인트가_잔액보다_크면_사용_실패")
    void useTest_사용하려는_포인트가_잔액보다_크면_사용_실패() {
        // given
        Long id = 1L;
        Long useAmount = 2000L;
        UserPoint currentUserPoint = new UserPoint(1L, 1000L, 0L);

        // when
        when(userPointTable.selectById(id)).thenReturn(currentUserPoint);

        // then
        assertThrows(PointCustomException.class, () -> {
            pointService.use(id, useAmount);
        });
    }

    @Test
    @DisplayName("특정_유저의_포인트_사용_완료")
    void useTest_특정_유저의_포인트_사용_완료() {
        // given
        Long id = 1L;
        Long useAmount = 200L;
        UserPoint currentUserPoint = new UserPoint(1L, 1000L, 0L);
        UserPoint remainUserPoint = new UserPoint(1L, 800L, System.currentTimeMillis());

        // when
        when(userPointTable.selectById(id)).thenReturn(currentUserPoint);
        when(userPointTable.insertOrUpdate(currentUserPoint.id(), currentUserPoint.point() - useAmount)).thenReturn(remainUserPoint);
        UserPoint result = pointService.use(id, useAmount);

        // then
        assertNotNull(result);
        assertEquals(result.point(), 800L);
    }

    @Test
    @DisplayName("특정_유저가_포인트_사용하면_포인트_이용_내역_추가")
    void useTest_특정_유저가_포인트_사용하면_포인트_이용_내역_추가() {
        Long id = 1L;
        Long useAmount = 200L;
        UserPoint currentUserPoint = new UserPoint(1L, 1000L, 0L);
        UserPoint remainUserPoint = new UserPoint(1L, 800L, System.currentTimeMillis());

        // when
        when(userPointTable.selectById(id)).thenReturn(currentUserPoint);
        when(userPointTable.insertOrUpdate(currentUserPoint.id(), currentUserPoint.point() - useAmount)).thenReturn(remainUserPoint);
        pointService.use(id, useAmount);

        // then
        verify(pointHistoryTable).insert(anyLong(), anyLong(), any(), anyLong());
    }

    @Test
    @DisplayName("특정_유저의_포인트_충전_이용_내역_조회")
    void findHistoryListTest_특정_유저의_포인트_충전_이용_내역_조회() {
        // given
        Long id = 1L;
        List<PointHistory> pointHistoryList = List.of(
                new PointHistory(1L, 1L, 1000L, TransactionType.CHARGE, 0L),
                new PointHistory(2L, 1L, 300L, TransactionType.USE, 1L)
        );

        // when
        when(pointHistoryTable.selectAllByUserId(id)).thenReturn(pointHistoryList);
        List<PointHistory> result = pointService.findHistoryList(id);

        // then
        assertNotNull(result);
        assertEquals(result.size(), 2);
    }
}