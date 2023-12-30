package com.example.librarysystemadmin.controller;


import com.example.librarysystemadmin.domain.Statistic;
import com.example.librarysystemadmin.service.StatisticService;
import com.example.librarysystemadmin.utils.ApiResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Api/log")
public class StatisticsController {
    @Autowired
    private StatisticService statisticsService;

    @RequestMapping("/getLogList")
    public ApiResponse<Statistic[]> getLogList(@Param("limit") int limit) {
        ApiResponse<Statistic[]> apiResponse = new ApiResponse<>();
        Statistic[] statistics = statisticsService.getLogList(limit);
        apiResponse.setSuccessResponse(statistics);
        return apiResponse;
    }
}
