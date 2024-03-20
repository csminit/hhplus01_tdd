package io.hhplus.tdd.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private PointService pointService;

    @Test
    @DisplayName("특정_유저의_포인트를_조회")
    public void pointTest() throws Exception {
        // given
        Long id = 1L;
        UserPoint userPoint = new UserPoint(1L, 1000L, 0L);
        given(pointService.findPoint(id)).willReturn(userPoint);

        // when - then
        mockMvc.perform(get("/point/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포인트_이용_내역_조회")
    public void historyTest() throws Exception {
        // given
        Long userId = 1L;
        List<PointHistory> historyList = Collections.emptyList();
        given(pointService.findHistoryList(userId)).willReturn(historyList);

        // when - then
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포인트_충전")
    public void chargeTest() throws Exception {
        // given
        Long id = 1L;
        Long amount = 500L;
        UserPoint updatedPoint = new UserPoint(id, amount, 0L);
        given(pointService.charge(id, amount)).willReturn(updatedPoint);

        // when
        mockMvc.perform(MockMvcRequestBuilders.patch("/point/{id}/charge", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("amount", amount))));
    }

    @Test
    @DisplayName("포인트_사용")
    public void useTest() throws Exception {
        // given
        Long id = 1L;
        Long amount = 500L;
        UserPoint updatedPoint = new UserPoint(id, amount, 0L);
        given(pointService.use(id, amount)).willReturn(updatedPoint);

        // when
        mockMvc.perform(MockMvcRequestBuilders.patch("/point/{id}/use", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("amount", amount))));
    }
}
