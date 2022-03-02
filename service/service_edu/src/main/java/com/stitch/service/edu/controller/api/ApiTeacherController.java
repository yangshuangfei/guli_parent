package com.stitch.service.edu.controller.api;

import com.stitch.common.base.result.R;
import com.stitch.service.edu.entity.Teacher;
import com.stitch.service.edu.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin
@RestController
@Slf4j
@Api(description = "首页讲师管理")
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {

    @Autowired
    private ITeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("list")
    public R listAll(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list).message("获取讲师列表成功");
    }
    @ApiOperation(value = "根据讲师ID获取讲师信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "讲师Id",required = true)
            @PathVariable String id){
        Map<String,Object> map = teacherService.selectTeacherInfoById(id);
        return R.ok().data(map).message("查询讲师信息成功");
//        return map;
    }

}
