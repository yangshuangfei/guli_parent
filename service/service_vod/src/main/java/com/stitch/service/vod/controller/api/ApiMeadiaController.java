package com.stitch.service.vod.controller.api;

import com.stitch.common.base.result.R;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.common.base.util.ExceptionUtils;
import com.stitch.service.base.exception.StitchException;
import com.stitch.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "阿里云视频点播")
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("api/vod/video")
public class ApiMeadiaController {
    @Autowired
    private VideoService videoService;

    @GetMapping("get-play-auth/{videoSourceId}")
    public R getPlayAuth(
            @ApiParam(value = "阿里云视频文件的id", required = true)
            @PathVariable String videoSourceId){

        try{
            String playAuth = videoService.getPlayAuth(videoSourceId);
            return  R.ok().message("获取播放凭证成功").data("playAuth", playAuth);
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new StitchException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }
    }
}
