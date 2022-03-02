package com.stitch.service.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stitch.common.base.result.R;
import com.stitch.service.statistics.entity.Daily;
import com.stitch.service.statistics.feign.UcenterMemberService;
import com.stitch.service.statistics.mapper.DailyMapper;
import com.stitch.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2021-03-28
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private UcenterMemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createStatisticsByDay(String day) {
        //如果当日统计信息已存在，则删除记录
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", day);
        baseMapper.delete(queryWrapper);

        R r = memberService.countRegisterNum(day);
        Integer registerNum = (Integer) r.getData().get("registerNum");

        int loginNum = RandomUtils.nextInt(100, 200);
        int videoViewNum = RandomUtils.nextInt(150, 200);
        int courseNum = RandomUtils.nextInt(50, 200);


        //创建统计数据对象
        Daily daily = new Daily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Map<String, Object>> getChartData(String beginTime, String endTime) {
        //学员登录数据统计
        Map<String, Object> login_num = getChartDataByType(beginTime, endTime, "login_num");
        //学员注册统计
        Map<String, Object> register_num = getChartDataByType(beginTime, endTime, "register_num");
        //课程播放数统计
        Map<String, Object> video_view_num = getChartDataByType(beginTime, endTime, "video_view_num");
        //每日新增课程数统计
        Map<String, Object> course_num = getChartDataByType(beginTime, endTime, "course_num");

        HashMap<String, Map<String, Object>> hashMap = new HashMap<>();
        hashMap.put("registerNum",register_num);
        hashMap.put("loginNum",login_num);
        hashMap.put("videoViewNum",video_view_num);
        hashMap.put("courseNum",course_num);
        return hashMap;
    }


    public Map<String, Object> getChartDataByType(String beginTime, String endTime, String type) {
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<String> xList = new ArrayList<>();//x轴数据
        ArrayList<Integer> yList = new ArrayList<>();//y轴数据

        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("date_calculated", type);
        queryWrapper.between("date_calculated", beginTime, endTime);

        List<Map<String, Object>> mapList = baseMapper.selectMaps(queryWrapper);
        for (Map<String, Object> data : mapList) {
            String dateCalculated = (String) data.get("date_calculated");
            xList.add(dateCalculated);

            Integer count = (Integer) data.get(type);
            yList.add(count);
        }
        map.put("xData",xList);
        map.put("yData",yList);
        return map;
    }
}
