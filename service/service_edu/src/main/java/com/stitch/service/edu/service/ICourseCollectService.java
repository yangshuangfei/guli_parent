package com.stitch.service.edu.service;

import com.stitch.service.edu.entity.CourseCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stitch.service.edu.entity.vo.CourseCollectVo;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
public interface ICourseCollectService extends IService<CourseCollect> {

    List<CourseCollectVo> selectListByMemberId(String memberId);

    Boolean collect(String courseId, String id);
}
