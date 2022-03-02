package com.stitch.service.edu.mapper;

import com.stitch.service.edu.entity.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stitch.service.edu.entity.vo.SubjectVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Repository
public interface SubjectMapper extends BaseMapper<Subject> {

    List<SubjectVo> selectNestedListByParentId(String parentId);
}
