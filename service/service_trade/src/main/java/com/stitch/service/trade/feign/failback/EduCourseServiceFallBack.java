package com.stitch.service.trade.feign.failback;

import com.stitch.common.base.result.R;
import com.stitch.service.base.dto.CourseDto;
import com.stitch.service.trade.feign.EduCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EduCourseServiceFallBack implements EduCourseService {
    @Override
    public CourseDto getCourseDtoByCourseId(String courseId) {
        log.info("熔断保护");
        return null;
    }

    @Override
    public R updateBuyCountById(String courseId) {
        log.info("熔断保护");
        return R.error();
    }
}
