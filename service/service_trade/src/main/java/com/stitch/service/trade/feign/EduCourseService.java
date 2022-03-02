package com.stitch.service.trade.feign;

import com.stitch.common.base.result.R;
import com.stitch.service.base.dto.CourseDto;
import com.stitch.service.trade.feign.failback.EduCourseServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(value = "service-edu",fallback = EduCourseServiceFallBack.class)
public interface EduCourseService {
    @GetMapping("/api/edu/course/inner/get-course-dto/{courseId}")
    CourseDto getCourseDtoByCourseId(@PathVariable(value = "courseId") String courseId);
    @GetMapping("/api/edu/course/inner/update-buy-count/{courseId}")
    R updateBuyCountById(@PathVariable(value = "courseId") String courseId);
}
