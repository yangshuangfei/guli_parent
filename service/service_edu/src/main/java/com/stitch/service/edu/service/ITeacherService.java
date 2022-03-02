package com.stitch.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stitch.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stitch.service.edu.entity.vo.TeacherQueryVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
public interface ITeacherService extends IService<Teacher> {

    IPage<Teacher> selectPage(Page<Teacher> teacherPage, TeacherQueryVo teacherQueryVo);

    List<Map<String, Object>> selectNameListByKey(String nameKey);

    boolean removeAvatarById(String id);
    Map<String,Object> selectTeacherInfoById(String id);

    List<Teacher> selectHotTeacher();
}
