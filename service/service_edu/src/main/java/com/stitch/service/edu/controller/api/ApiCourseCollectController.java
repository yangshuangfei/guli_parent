package com.stitch.service.edu.controller.api;

import com.stitch.common.base.result.R;
import com.stitch.common.base.util.JwtInfo;
import com.stitch.common.base.util.JwtUtils;
import com.stitch.service.edu.entity.vo.CourseCollectVo;
import com.stitch.service.edu.service.ICourseCollectService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "收藏课程管理")
//@CrossOrigin
@RestController
@RequestMapping("/api/edu/course-collect")
@Slf4j
public class ApiCourseCollectController {
    @Autowired
    private ICourseCollectService courseCollectService;

    @GetMapping("auth/list")
    public R getCourseCollectListByMemberId(HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<CourseCollectVo> collectVos = courseCollectService.selectListByMemberId(jwtInfo.getId());
        return R.ok().data("items",collectVos);
    }

    @PostMapping("auth/collect/{courseId}")
    public R collect(@PathVariable String courseId, HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        Boolean result = courseCollectService.collect(courseId,jwtInfo.getId());
        if (result){
            return R.ok().message("收藏成功");
        } else {
            return R.error().message("收藏失败");
        }
    }

}
