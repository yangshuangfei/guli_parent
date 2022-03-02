package com.stitch.service.edu.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stitch.common.base.result.R;
import com.stitch.service.edu.entity.Teacher;
import com.stitch.service.edu.entity.vo.TeacherQueryVo;
import com.stitch.service.edu.feign.OSSFileService;
import com.stitch.service.edu.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @see
 * @author ysf
 * @since 2020-12-20
 */
//@CrossOrigin //有了网关后就可以删除这个注解了
@Api(description = "讲师管理")
@Slf4j
@RestController
@RequestMapping("admin/edu/teacher")
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private OSSFileService ossFileService; //远程调用

    @ApiOperation("所有讲师列表")
    @GetMapping("list")
    public R listAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "根据ID删除讲师", notes = "根据ID删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "讲师ID") @PathVariable String id) {
        //删除讲师图像
        boolean b = teacherService.removeAvatarById(id);

        //删除讲师
        boolean result = teacherService.removeById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation(value = "根据ID列表删除讲师", notes = "根据ID列表删除讲师")
    @DeleteMapping("batchRemove")
    public R removeById(@ApiParam(value = "讲师ID列表") @RequestBody List<String> idList) {
        boolean result = teacherService.removeByIds(idList);
        if (result) {
            return R.ok().message("批量删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("讲师分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable(required = true) Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable(required = true) Long limit,
                      @ApiParam(value = "讲师查询对象", required = true) TeacherQueryVo teacherQueryVo) {
        Page<Teacher> teacherPage = new Page<>(page, limit);
        IPage<Teacher> page1 = teacherService.selectPage(teacherPage, teacherQueryVo);
        List<Teacher> records = page1.getRecords();
        long total = page1.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(@ApiParam("讲师对象") @RequestBody() Teacher teacher) {
        teacherService.save(teacher);
        return R.ok().message("保存成功");
    }

    @ApiOperation("更新讲师")
    @PutMapping("update")
    public R updateById(@ApiParam("讲师对象") @RequestBody Teacher teacher) {
        teacherService.updateById(teacher);
        return R.ok().message("更新成功");
    }

    @ApiOperation("根据id获取讲师信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam("讲师ID") @PathVariable("id") String id) {
        Teacher teacher = teacherService.getById(id);
        if (teacher != null) {
            return R.ok().data("teacher", teacher).message("获取成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据key值查询姓名列表")
    @GetMapping("list/name/{nameKey}")
    public R selectNameListByKey(@ApiParam("关键字") @PathVariable("nameKey") String nameKey) {
        List<Map<String, Object>> list = teacherService.selectNameListByKey(nameKey);
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "service-edu服务调用service-oss测试")
    @GetMapping("test")
    public R test() {
        ossFileService.test();
        return R.ok();
    }

    @ApiOperation("测试并发")
    @GetMapping("test_concurrent")
    public R testConcurrent() {
        log.info("test_concurrent");
        return R.ok();
    }

    @GetMapping("/message1")
    public String message1() {
        return "message1";
    }

    @GetMapping("/message2")
    public String message2() {
        return "message2";
    }
}
