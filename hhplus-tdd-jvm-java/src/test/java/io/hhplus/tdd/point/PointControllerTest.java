package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PointControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserPointTable userPointTable;
    @Autowired
    private PointHistoryTable pointHistoryTable;

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String PATH = "/point";

    @BeforeEach
    void setup() {
        // 유저 포인트 데이터 생성
        userPointTable.insertOrUpdate(1L, 1000L);
        pointHistoryTable.insert(1L, 1000L, TransactionType.CHARGE, 0L);
    }

    @Test
    @DisplayName("특정_유저의_포인트를_조회")
    void pointTest_특정_유저의_포인트를_조회() {
        // given
        Long id = 1L;

        // when
        ResponseEntity<UserPoint> response = restTemplate.getForEntity(LOCAL_HOST + port + PATH + "/" + id, UserPoint.class);

        // then
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().point()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("포인트_이용_내역_조회")
    void historyTest_포인트_이용_내역_조회() {
        // given
        Long id = 1L;

        // when
        ResponseEntity<List<PointHistory>> response = restTemplate.exchange(
                LOCAL_HOST + port + PATH + "/" + id + "/histories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PointHistory>>() {}
        );

        // then
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0).amount()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("포인트_충전")
    void chargeTest_포인트_충전() {
        // given
        Long id = 1L;
        Long amount = 5000L;

        // when
        // TODO : restTemplate은 patch 지원하지 않음
        UserPoint response = restTemplate.patchForObject(LOCAL_HOST + port + PATH + "/" + id + "/charge", amount, UserPoint.class);

        // then
        assertThat(response.point()).isEqualTo(6000L);
    }

    @Test
    @DisplayName("포인트_사용")
    void useTest_포인트_사용() {
    }

    // TODO
    // 1. 통합테스트 더 마무리 + 실패 케이스도
    // 2. 동시성 제어 코드 추가
    // 3. 사전 노트 작성
}