package com.stitch.service.statistics.controller;


import com.stitch.common.base.result.R;
import com.stitch.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author ysf
 * @since 2021-03-28
 */
@Api("统计分析管理")
@RestController
@RequestMapping("admin/statistics/daily")
public class DailyController {
    @Autowired
    private DailyService dailyService;

    @ApiOperation("生成统计数据")
    @PostMapping("create/{day}")
    public R createStatisticsByDay(@PathVariable String day) {
        dailyService.createStatisticsByDay(day);
        return R.ok().message("统计数据生成成功");
    }
    @ApiOperation("显示统计数据")
    @GetMapping("show-chart/{begin}/{end}")
    public R showChart(
            @ApiParam("开始时间") @PathVariable String begin,
            @ApiParam("结束时间") @PathVariable String end){

        Map<String, Map<String, Object>> map = dailyService.getChartData(begin, end);
        return R.ok().data("chartData", map);
    }
}

