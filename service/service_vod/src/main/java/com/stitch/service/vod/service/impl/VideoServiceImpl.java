package com.stitch.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.service.base.exception.StitchException;
import com.stitch.service.vod.service.VideoService;
import com.stitch.service.vod.util.AliyunVodSDKUtils;
import com.stitch.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VodProperties vodProperties;

    @Override
    public String uploadVideo(InputStream inputStream, String originalFilename) {
        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        UploadStreamRequest request = new UploadStreamRequest(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret(),
                title, originalFilename, inputStream
        );
        //模板组ID，可选
//        request.setTemplateGroupId(vodProperties.getTemplateGroupId());
        //工作流ID，可选
//        request.setWorkflowId(vodProperties.getWorkflowId());
        UploadVideoImpl uploadVideo = new UploadVideoImpl();
        UploadStreamResponse streamResponse = uploadVideo.uploadStream(request);

        String videoId = streamResponse.getVideoId();
        if (StringUtils.isEmpty(videoId)) {
            log.error("阿里云上传失败：" + streamResponse.getCode() + "-" + streamResponse.getMessage());
            throw new StitchException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
        return videoId;
    }

    @Override
    public void removeVideo(String videoId) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔，最多20个
        request.setVideoIds(videoId);
        client.getAcsResponse(request);
    }

    @Override
    public void removeVideoByIdList(List<String> videoIdList) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔，最多20个
        StringBuffer buffer = new StringBuffer();
//        videoIdList.stream().forEach(t -> buffer.append(t).append(","));
//        String videoIds = buffer.substring(0,videoIdList.size()-2);
        int size = videoIdList.size();
        for (int i = 0; i < size; i++) {
            buffer.append(videoIdList.get(i));
            if (i == size - 1 || i % 20 == 19) {
                log.info("buffer" + buffer.toString());
                request.setVideoIds(buffer.toString());
                client.getAcsResponse(request);
                //重置buffer
                buffer = new StringBuffer();
            } else if (i < 19) {
                buffer.append(",");
            }
        }

    }

    @Override
    public String getPlayAuth(String videoSourceId) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();//创建请求对象
        request.setVideoId(videoSourceId);//设置请求参数
//        request.setAuthInfoTimeout(200L);

        GetVideoPlayAuthResponse response = client.getAcsResponse(request);//发送请求得到响应

        return response.getPlayAuth();
    }
}
