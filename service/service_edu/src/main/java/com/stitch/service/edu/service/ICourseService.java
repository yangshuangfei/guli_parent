package com.stitch.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stitch.service.base.dto.CourseDto;
import com.stitch.service.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stitch.service.edu.entity.form.CourseInfoForm;
import com.stitch.service.edu.entity.vo.*;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
public interface ICourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoById(String id);

    void updateCourseInfoById(CourseInfoForm courseInfoForm);

    IPage<CourseVo> selectPage(Page<CourseVo> teacherPage, CourseQueryVo courseQueryVo);

    boolean removeCoverById(String id);

    boolean removeCourseById(String id);

    CoursePublishVo getCoursePublishVoById(String id);

    boolean publishCourseById(String id);

    List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo);

    WebCourseVo selectWebCourseVoById(String id);

    List<Course> selectHotCourse();

    CourseDto getCourseDtoByCourseId(String courseId);

    void updateBuyCountById(String id);
}
