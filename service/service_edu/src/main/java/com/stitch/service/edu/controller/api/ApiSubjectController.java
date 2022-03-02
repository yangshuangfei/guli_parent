package com.stitch.service.edu.controller.api;

import com.stitch.common.base.result.R;
import com.stitch.service.edu.entity.vo.SubjectVo;
import com.stitch.service.edu.service.ISubjectService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin
@RestController
@Slf4j
@Api(description = "首页课程分类管理")
@RequestMapping("/api/edu/subject")
public class ApiSubjectController {
    @Autowired
    private ISubjectService subjectService;
    
    @GetMapping("nested-list")
    public R nestedList(){
        List<SubjectVo> subjectVos = subjectService.nestedList();
        return R.ok().data("items",subjectVos);
    }
}
