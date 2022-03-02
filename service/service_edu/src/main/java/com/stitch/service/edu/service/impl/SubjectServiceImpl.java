package com.stitch.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.stitch.service.edu.entity.Subject;
import com.stitch.service.edu.entity.excel.ExcelSubjectData;
import com.stitch.service.edu.entity.vo.SubjectVo;
import com.stitch.service.edu.listener.ExcelSubjectDataListener;
import com.stitch.service.edu.mapper.SubjectMapper;
import com.stitch.service.edu.service.ISubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {

    @Autowired(required = false)
    SubjectMapper subjectMapper;

    @Override
    public void batchImport(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelSubjectDataListener(subjectMapper))
                .excelType(ExcelTypeEnum.XLS)
                .sheet().doRead();
    }

    @Override
    public List<SubjectVo> nestedList() {
        return baseMapper.selectNestedListByParentId("0");
    }
}
