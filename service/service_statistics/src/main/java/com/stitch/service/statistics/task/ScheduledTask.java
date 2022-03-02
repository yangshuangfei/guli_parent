package com.stitch.service.statistics.task;

import com.stitch.service.statistics.service.DailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTask {
    @Autowired
    private DailyService dailyService;
//    @Scheduled(cron = "0/3 * * * * *")
    public void testTask(){
        System.out.println("task被执行");
    }

    @Scheduled(cron = "0 10 22 * * ? ")
    public void testGenStatisticsData(){
        System.out.println("testGenStatisticsData被执行");
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        dailyService.createStatisticsByDay(day);
    }
}
