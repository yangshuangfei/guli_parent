package com.stitch.service.vod.controller;

import com.aliyuncs.exceptions.ClientException;
import com.stitch.common.base.result.R;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.common.base.util.ExceptionUtils;
import com.stitch.service.base.exception.StitchException;
import com.stitch.service.vod.service.VideoService;
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

@Api(description = "阿里云视频点播")
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/admin/vod/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PostMapping("upload")
    public R uploadVideo(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String videoId = videoService.uploadVideo(inputStream, originalFilename);
            return R.ok().data("videoId", videoId).message("视频上传成功");
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new StitchException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @ApiOperation("根据视频ID删除阿里云视频")
    @DeleteMapping("remove/{videoId}")
    public R removeVideo(
            @ApiParam(name = "videoId", value = "阿里云视频ID", required = true)
            @PathVariable String videoId) {
        try {
            log.info("删除阿里云视频");
            videoService.removeVideo(videoId);
            return R.ok().message("视频删除成功");
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new StitchException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }
    @ApiOperation("根据视频ID列表删除阿里云视频")
    @DeleteMapping("removeByIdList")
    public R removeVideoByIdList(@ApiParam(value = "阿里云视频id列表", required = true)
                                 @RequestBody List<String> videoIdList) {
        try {
            videoService.removeVideoByIdList(videoIdList);
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new StitchException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
        return R.ok().message("批量刪除成功");
    }

}
