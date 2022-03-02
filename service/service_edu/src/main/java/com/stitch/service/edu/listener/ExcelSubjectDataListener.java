package com.stitch.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stitch.service.edu.entity.Subject;
import com.stitch.service.edu.entity.excel.ExcelSubjectData;
import com.stitch.service.edu.mapper.SubjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {
    private SubjectMapper subjectMapper;

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        log.info("读取每一行数据");
        // 处理读取的数据
        String levelOneTitle = excelSubjectData.getLevelOneTitle();//一级标题
        String levelTwoTitle = excelSubjectData.getLevelTwoTitle();//二级标题

        Subject subjectLevelOne = this.getByTitle(levelOneTitle);
        String parentId = null;
        //判断数据是否存在
        if (subjectLevelOne == null) {
            Subject subjectOne = new Subject();
            subjectOne.setParentId("0");
            subjectOne.setTitle(levelOneTitle);
            int insertOne = subjectMapper.insert(subjectOne);
            parentId = subjectOne.getId();
        } else {
            parentId = subjectLevelOne.getId();
        }

        //判断二级类别是否存在
        Subject subjectLevelTwo = this.getSubByTitle(levelTwoTitle, parentId);
        if (subjectLevelTwo == null) {
            //组装二级类别
            Subject subjectTwo = new Subject();
            subjectTwo.setParentId(parentId);
            subjectTwo.setTitle(levelTwoTitle);
            int insertTwo = subjectMapper.insert(subjectTwo);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("读取数据完毕");
    }

    /**
     * 根据一级分类的名称查询数据是否存在
     *
     * @param title
     * @return
     */
    private Subject getByTitle(String title) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0"); //一级分类
        return subjectMapper.selectOne(queryWrapper);
    }

    /**
     * 根据分类的名称和父id查询数据是否存在
     *
     * @param title
     * @param parentId
     * @return
     */
    private Subject getSubByTitle(String title, String parentId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId); //二级分类
        return subjectMapper.selectOne(queryWrapper);
    }
}
