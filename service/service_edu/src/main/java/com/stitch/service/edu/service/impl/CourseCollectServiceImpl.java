package com.stitch.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stitch.service.edu.entity.CourseCollect;
import com.stitch.service.edu.entity.vo.CourseCollectVo;
import com.stitch.service.edu.mapper.CourseCollectMapper;
import com.stitch.service.edu.service.ICourseCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectMapper, CourseCollect> implements ICourseCollectService {

    @Override
    public List<CourseCollectVo> selectListByMemberId(String memberId) {

        return baseMapper.selectListByMemberId(memberId);
    }

    @Override
    public Boolean collect(String courseId, String id) {
        CourseCollect courseCollect = new CourseCollect();
        courseCollect.setCourseId(courseId);
        courseCollect.setMemberId(id);
        int insert = baseMapper.insert(courseCollect);
        return insert != 0;
    }
}
