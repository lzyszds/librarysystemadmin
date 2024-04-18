package com.example.librarysystemadmin;

import com.example.librarysystemadmin.config.LibraryConfig;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;


@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(LibraryConfig.class)
public class LibrarySystemAdminApplication implements SchedulingConfigurer {

    public static void main(String[] args) {

        SpringApplication.run(LibrarySystemAdminApplication.class, args);
    }


    @Autowired
    private Scheduler scheduler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        try {
            // 启动 Quartz 调度程序
            scheduler.start();
            // 添加任务
            JobDetail jobDetail = JobBuilder.newJob(DailyStatsJob.class)
                    .withIdentity("dailyStatsJob")
                    .storeDurably()
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity("dailyStatsTrigger")
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInHours(24)
                            .repeatForever())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public DailyStatsJob dailyStatsJob() {
        return new DailyStatsJob();
    }
}
