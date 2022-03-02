package com.stitch.service.edu.controller.api;

import com.stitch.common.base.result.R;
import com.stitch.service.edu.entity.Course;
import com.stitch.service.edu.entity.Teacher;
import com.stitch.service.edu.service.ICourseService;
import com.stitch.service.edu.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin
@RestController
@Slf4j
@Api(description = "首页")
@RequestMapping("/api/edu/index")
public class ApiIndexController {
    @Autowired
    private ICourseService courseService;

    @Autowired
    private ITeacherService teacherService;

    @ApiOperation("课程和讲师的首页数据")
    @GetMapping
    public R index(){
        //查询热门课程
       List<Course> courseList = courseService.selectHotCourse();

       //查询推荐讲师
        List<Teacher> teacherList = teacherService.selectHotTeacher();

        return  R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}
