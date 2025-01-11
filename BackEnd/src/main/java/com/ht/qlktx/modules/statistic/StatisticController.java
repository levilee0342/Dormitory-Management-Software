package com.ht.qlktx.modules.statistic;

import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.statistic.dtos.StatisticOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/overview")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<StatisticOverview>> getOverview() {
        var overview = statisticService.getOverview();
        return ResponseEntity.ok(
                Response.<StatisticOverview>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy thông tin tổng quan thành công")
                        .data(overview)
                        .build()
        );
    }
}
