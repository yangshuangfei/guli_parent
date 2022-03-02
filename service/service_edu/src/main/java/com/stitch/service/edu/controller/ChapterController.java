package com.stitch.service.edu.controller;


import com.stitch.common.base.result.R;
import com.stitch.service.edu.entity.Chapter;
import com.stitch.service.edu.entity.vo.ChapterVo;
import com.stitch.service.edu.service.IChapterService;
import com.stitch.service.edu.service.IVideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
//@CrossOrigin
@Slf4j
@RestController
@RequestMapping("admin/edu/chapter")
public class ChapterController {
    @Autowired
    IChapterService chapterService;

    @Autowired
    IVideoService videoService;

    @ApiOperation("新增章节")
    @PostMapping("save")
    public R saveChapter(@ApiParam(value = "章节对象", required = true)
                         @RequestBody Chapter chapter) {
        boolean save = chapterService.save(chapter);
        if (save) {
            return R.ok().message("新增成功");
        } else {
            return R.error().message("新增失败");
        }
    }

    @ApiOperation("根据ID删除章节")
    @DeleteMapping("remove/{id}")
    public R removeChapterById(@ApiParam(value = "章节ID")
                               @PathVariable("id") String id) {
        //刪除课程视频
        videoService.removeVideoByChapterId(id);

        //删除章节
        boolean b = chapterService.removeChapterById(id);
        if (b) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("根据ID获取章节信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "章节ID")
                     @PathVariable("id") String id) {
        Chapter chapter = chapterService.getById(id);
        if (chapter != null) {
            return R.ok().data("item", chapter).message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID更新章节信息")
    @PutMapping("update")
    public R updateById(@ApiParam(value = "章节对象", required = true)
                        @RequestBody Chapter chapter) {
        boolean b = chapterService.updateById(chapter);
        if (b) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("查询章节列表")
    @GetMapping("nested-list/{courseId}")
    public R nestedList(@ApiParam(value = "课程Id",required = true)
            @PathVariable String courseId) {
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);
        return R.ok().data("items", chapterVoList);
    }
}

