package com.stitch.service.oss.controller;

import com.stitch.common.base.result.R;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.common.base.util.ExceptionUtils;
import com.stitch.service.base.exception.StitchException;
import com.stitch.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@CrossOrigin
@Api(description = "阿里云文件管理")
@Slf4j
@RequestMapping("admin/oss/file")
public class FileController {
    @Autowired
    FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(
            @ApiParam(name = "file", value = "多部分文件", required = true)
            @RequestParam("file")
                    MultipartFile file,
            @ApiParam(value = "模块", required = true)
            @RequestParam("module")
                    String module) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream, module, originalFilename);
            log.info(uploadUrl);
            return R.ok().message("文件上传成功").data("url", uploadUrl);
        } catch (Exception e) {
            throw new StitchException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation(value = "测试")
    @GetMapping("test")
    public R test() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new StitchException(e.getMessage(),null);
        }
        log.info("oss test 被调用");
        return R.ok();
    }

    @ApiOperation(value = "测试删除文件")
    @DeleteMapping("removeFile")
    public R removeFile(
            @ApiParam(name = "url", value = "要删除的文件路径", required = true)
            @RequestBody String url) {
        fileService.removeFile(url);
        return R.ok();
    }
}
