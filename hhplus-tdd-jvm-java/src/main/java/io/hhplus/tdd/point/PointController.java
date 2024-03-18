package io.hhplus.tdd.point;

import io.hhplus.tdd.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequestMapping("/point")
@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService service;

    @GetMapping("{id}")
    public UserPoint point(@PathVariable Long id) {
        return service.findPoint(id);
    }

    @GetMapping("{id}/histories")
    public List<PointHistory> history(@PathVariable Long id) {
        return service.findHistoryList(id);
    }

    @PatchMapping("{id}/charge")
    public UserPoint charge(@PathVariable Long id, @RequestBody Long amount) {
        return service.charge(id, amount);
    }

    @PatchMapping("{id}/use")
    public UserPoint use(@PathVariable Long id, @RequestBody Long amount) {
        return service.use(id, amount);
    }
}
