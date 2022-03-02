package com.stitch.service.statistics.service;

import com.stitch.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author ysf
 * @since 2021-03-28
 */
public interface DailyService extends IService<Daily> {
    void createStatisticsByDay(String day);

    Map<String,Map<String, Object>> getChartData(String beginTime,String endTime);
}
