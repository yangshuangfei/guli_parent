package com.stitch.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stitch.common.base.result.R;
import com.stitch.service.edu.entity.Course;
import com.stitch.service.edu.entity.Teacher;
import com.stitch.service.edu.entity.vo.TeacherQueryVo;
import com.stitch.service.edu.feign.OSSFileService;
import com.stitch.service.edu.mapper.CourseMapper;
import com.stitch.service.edu.mapper.TeacherMapper;
import com.stitch.service.edu.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Service
@Slf4j
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {
    @Autowired
    private OSSFileService ossFileService;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public IPage<Teacher> selectPage(Page<Teacher> teacherPage, TeacherQueryVo teacherQueryVo) {
        //1.排序问题：按照sort字段排序
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByAsc("sort");//设置排序

        //2.查询
        if (teacherQueryVo == null) {
            return baseMapper.selectPage(teacherPage, teacherQueryWrapper);
        }
//        3.条件查询
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();

        if (!StringUtils.isEmpty(name)) {
            teacherQueryWrapper.likeRight("name", name);
        }
        if (level != null) {
            teacherQueryWrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(joinDateBegin)) {
            teacherQueryWrapper.ge("join_date", joinDateBegin);
        }
        if (!StringUtils.isEmpty(joinDateEnd)) {
            teacherQueryWrapper.le("join_date", joinDateEnd);
        }

        return baseMapper.selectPage(teacherPage, teacherQueryWrapper);
    }

    @Override
    public List<Map<String, Object>> selectNameListByKey(String nameKey) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name").likeRight("name", nameKey);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        return maps;
    }

    @Override
    public boolean removeAvatarById(String id) {
        //根据id获取讲师的图像路径
        Teacher teacher = baseMapper.selectById(id);
        if (teacher != null) {
            String avatar = teacher.getAvatar();
            if (!StringUtils.isEmpty(avatar)) {
                log.info("远程调用删除用户avatar");
                R r =  ossFileService.removeFile(avatar);
                return r.getSuccess(); //熔断保护会返回fasle,只是controller没控制
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {
        Teacher teacher = baseMapper.selectById(id);
        Map<String,Object> map = null;
        if(teacher != null){
            QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
            courseQueryWrapper.eq("teacher_id",id);
            List<Course> courses = courseMapper.selectList(courseQueryWrapper);
            map = new HashMap<>();
            map.put("teacher",teacher);
            map.put("courseList",courses);
        }
        return map;
    }

    @Cacheable(value = "index",key = "'selectHotTeacher'")
    @Override
    public List<Teacher> selectHotTeacher() {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort");
        queryWrapper.last("limit 4");
        return baseMapper.selectList(queryWrapper);
    }

}
