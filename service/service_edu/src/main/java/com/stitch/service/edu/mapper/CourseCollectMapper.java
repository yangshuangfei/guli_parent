package com.stitch.service.edu.mapper;

import com.stitch.service.edu.entity.CourseCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stitch.service.edu.entity.vo.CourseCollectVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程收藏 Mapper 接口
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Repository
public interface CourseCollectMapper extends BaseMapper<CourseCollect> {

    List<CourseCollectVo> selectListByMemberId(String memberId);
}
