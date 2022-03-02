package com.stitch.service.edu.controller;


import com.stitch.common.base.result.R;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.common.base.util.ExceptionUtils;
import com.stitch.service.base.exception.StitchException;
import com.stitch.service.edu.entity.vo.SubjectVo;
import com.stitch.service.edu.service.ISubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
//@CrossOrigin
@Api(description = "讲师管理")
@Slf4j
@RestController
@RequestMapping("admin/edu/subject")
public class SubjectController {
    @Autowired
    ISubjectService subjectService;

    @ApiOperation("Excel批量导入课程分类")
    @PostMapping("import")
    public R batchImport(
            @ApiParam(value = "Excel文件", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("批量导入成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new StitchException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }
    @ApiOperation("")
    @GetMapping("nested-list")
    public R nestedList() {
        List<SubjectVo> snestedList = subjectService.nestedList();
        return R.ok().data("items", snestedList);
    }
}

