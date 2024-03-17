package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PointControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private PointController pointController;
    @Autowired
    private UserPointTable userPointTable;

    @BeforeEach
    void setup() {
        // 유저 포인트 데이터 생성
//        UserPoint userPoint = new UserPoint(1L, 100L, System.currentTimeMillis());
    }

    @Test
    void point() {
        // given
        Long id = 1L;

        // when
        ResponseEntity<UserPoint> response = restTemplate.getForEntity("http://localhost:" + port + "/point/" + id, UserPoint.class);

        // then
        assertThat(response.getBody()).isEqualTo(new UserPoint(0L, 0L, 0L));
    }

    @Test
    void history() {
    }

    @Test
    void charge() {
    }

    @Test
    void use() {
    }
}