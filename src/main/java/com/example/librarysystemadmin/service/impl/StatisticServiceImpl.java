package com.example.librarysystemadmin.service.impl;


import com.example.librarysystemadmin.mapper.StatisticsLogMapper;
import com.example.librarysystemadmin.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    StatisticsLogMapper statisticsLogMapper;

    /*
     * 设置统计日志库表
     * */
    public void setStatisticsHandle(String type) {
        Date newDate = new Date(System.currentTimeMillis());
        switch (type) {
            case "user":
                statisticsLogMapper.updateNewUsersCount(newDate);
                break;
            case "book":
                statisticsLogMapper.updateNewBooksCount(newDate);
                break;
            case "borrowed":
                statisticsLogMapper.updateBooksReturnedCount(newDate);
                break;
            case "returned":
                statisticsLogMapper.updateBooksReturnedCount(newDate);
                break;
            case "visits":
                statisticsLogMapper.updateVisitsCount(newDate);
                break;
        }
    }
}
