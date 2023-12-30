package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.Statistic;

public interface StatisticService {

    void setStatisticsHandle(String type);

    Statistic[] getLogList(int limit);
}
