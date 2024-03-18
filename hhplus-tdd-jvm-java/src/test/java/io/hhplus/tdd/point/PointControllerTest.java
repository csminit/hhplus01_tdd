package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.anyOf;
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

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String PATH = "/point";

    @BeforeEach
    void setup() {
        // 유저 포인트 데이터 생성
        userPointTable.insertOrUpdate(1L, 1000L);
    }

    @Test
    void 특정_유저의_포인트를_조회() {
        // given
        Long id = 1L;

        // when
        ResponseEntity<UserPoint> response = restTemplate.getForEntity(LOCAL_HOST + port + PATH + "/" + id, UserPoint.class);

        // then
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().point()).isEqualTo(1000L);
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