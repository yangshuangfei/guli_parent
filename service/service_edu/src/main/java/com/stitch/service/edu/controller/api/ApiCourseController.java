package com.stitch.service.edu.controller.api;

import com.stitch.common.base.result.R;
import com.stitch.service.base.dto.CourseDto;
import com.stitch.service.edu.entity.Course;
import com.stitch.service.edu.entity.vo.ChapterVo;
import com.stitch.service.edu.entity.vo.WebCourseQueryVo;
import com.stitch.service.edu.entity.vo.WebCourseVo;
import com.stitch.service.edu.service.IChapterService;
import com.stitch.service.edu.service.ICourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin
@RestController
@Slf4j
@Api(description = "首页课程管理")
@RequestMapping("/api/edu/course")
public class ApiCourseController {
    @Autowired
    private ICourseService courseService;

    @Autowired
    private IChapterService chapterService;

    @ApiOperation("课程列表")
    @GetMapping("list")
    public R pageList(
            @ApiParam(value = "查询对象", required = true)
                    WebCourseQueryVo webCourseQueryVo) {
        List<Course> courseList = courseService.webSelectList(webCourseQueryVo);
        return R.ok().data("courseList", courseList);
    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("get/{courseId}")
    public R getById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String courseId) {
        //查询课程信息和讲师信息
        WebCourseVo webCourseVo = courseService.selectWebCourseVoById(courseId);

        //查询当前课程的嵌套章节和课时信息
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);
        return R.ok().data("course", webCourseVo).data("chapterVoList", chapterVoList).message("查询成功");
    }

    @ApiOperation("")
    @GetMapping("/inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoByCourseId(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable(value = "courseId") String courseId) {
        System.out.println("进入getCourseDtoByCourseId");
        CourseDto courseDto = courseService.getCourseDtoByCourseId(courseId);
        System.out.println(courseDto.toString());
        return courseDto;

    }

    @ApiOperation("根据课程ID更改销售量")
    @GetMapping("inner/update-buy-count/{courseId}")
    public R updateBuyCountById(
            @ApiParam(value = "课程Id", required = true)
            @PathVariable String courseId) {
        courseService.updateBuyCountById(courseId);
        return R.ok();
    }

}
