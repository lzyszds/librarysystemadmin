package com.example.librarysystemadmin;

import com.example.librarysystemadmin.mapper.StatisticsLogMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;


import java.text.SimpleDateFormat;

public class DailyStatsJob implements Job {

    @Autowired
    private StatisticsLogMapper statisticsLogMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 执行作业 每天统计系统数据 （用户数，图书数，借书数，还书数，访问数）
        System.out.println("统计系统数据");
        // 获取当前时间 YYYY-MM-DD
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = sdf.format(new java.util.Date());
        // 判断是否创建过当天的初始统计数据
        System.out.println("统计表新增初始数据: " + newDate);
        String count = statisticsLogMapper.getStatisticsLogByDate(newDate);
        if (count != null) return;
        statisticsLogMapper.insertStatisticsLog(newDate, 0, 0, 0, 0, 0);
    }

}