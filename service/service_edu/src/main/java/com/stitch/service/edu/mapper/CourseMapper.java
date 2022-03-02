package com.stitch.service.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stitch.service.edu.entity.Course;
import com.stitch.service.edu.entity.vo.CoursePublishVo;
import com.stitch.service.edu.entity.vo.CourseVo;
import com.stitch.service.edu.entity.vo.WebCourseVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Repository
public interface CourseMapper extends BaseMapper<Course> {

    List<CourseVo> selectPageByCourseQueryVo(Page<CourseVo> courseVoPage,
                                             @Param(Constants.WRAPPER) QueryWrapper<CourseVo> courseQueryWrapper);

    CoursePublishVo selectCoursePublishVoById(String id);

    WebCourseVo selectWebCourseVoById(String courseId);
}
