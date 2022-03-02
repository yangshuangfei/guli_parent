package com.stitch.service.edu.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stitch.common.base.result.R;
import com.stitch.service.edu.entity.form.CourseInfoForm;
import com.stitch.service.edu.entity.vo.CoursePublishVo;
import com.stitch.service.edu.entity.vo.CourseQueryVo;
import com.stitch.service.edu.entity.vo.CourseVo;
import com.stitch.service.edu.service.ICourseService;
import com.stitch.service.edu.service.IVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */

@Api(description = "课程管理")
@CrossOrigin
@RestController
@RequestMapping("admin/edu/course")
@Slf4j
public class CourseController {
    @Autowired
    private ICourseService courseService;
    @Autowired
    private IVideoService videoService;

    @ApiOperation("新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm) {
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId).message("保存成功");
    }

    @ApiOperation("获取课程表单信息")
    @GetMapping("course-info/{id}")
    public R getCourseInfoById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String id) {
        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);
        return R.ok().data("item", courseInfoForm).message("查询成功");
    }

    @ApiOperation("更新课程信息")
    @PutMapping("update-course-info")
    public R updateCourseInfoById(
            @ApiParam(value = "课程对象", required = true)
            @RequestBody CourseInfoForm courseInfoForm) {
        courseService.updateCourseInfoById(courseInfoForm);
        return R.ok().message("更新成功");
    }

    @ApiOperation("课程分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable(required = true) Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable(required = true) Long limit,
                      @ApiParam(value = "讲师查询对象", required = true) CourseQueryVo courseQueryVo) {
        Page<CourseVo> courseVoPage = new Page<>(page, limit);
        IPage<CourseVo> page1 = courseService.selectPage(courseVoPage, courseQueryVo);
        List<CourseVo> records = page1.getRecords();
        long total = page1.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation("根据ID删除课程")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "课程id", required = true)
                        @PathVariable String id) {
        //删除课程视频
        videoService.removeVideoByCourseId(id);

        //删除课程封面
        courseService.removeCoverById(id);

        //删除课程
        boolean result = courseService.removeCourseById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID获取课程发布信息")
    @GetMapping("course-publish/{id}")
    public R getCoursePublishVoById(@ApiParam(value = "课程ID", required = true)
                                    @PathVariable String id) {
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(id);
        if (coursePublishVo != null) {
            return R.ok().data("item", coursePublishVo);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id发布课程")
    @PutMapping("publish-course/{id}")
    public R publisCourseById(@ApiParam(value = "课程ID", required = true)
                              @PathVariable String id) {
        boolean result = courseService.publishCourseById(id);
        if (result) {
            return R.ok().message("发布成功");
        } else {
            return R.error().message("数据不存在");
        }
    }
}

