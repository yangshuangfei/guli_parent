package com.stitch.service.edu.service;

import com.stitch.service.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stitch.service.edu.entity.vo.SubjectVo;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
public interface ISubjectService extends IService<Subject> {
    void batchImport(InputStream inputStream);

    List<SubjectVo> nestedList();
}
