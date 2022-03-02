package com.stitch.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stitch.service.edu.entity.Video;
import com.stitch.service.edu.feign.VodVideoService;
import com.stitch.service.edu.mapper.VideoMapper;
import com.stitch.service.edu.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {
    @Autowired
    VodVideoService vodVideoService;

    @Override
    public void removeVideoById(String id) {
        //根据id找到视频id
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        log.warn("VideoServiceImpl：videoSourceId= " + videoSourceId);
        vodVideoService.removeVideo(videoSourceId);
    }

    @Override
    public void removeVideoByChapterId(String chapterId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("chapter_id",chapterId);

        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        List<String> strings = new ArrayList<>();
        maps.stream().forEach(map -> {
            String videoSourceId = map.get("video_source_id").toString();
            strings.add(videoSourceId);
        });
        vodVideoService.removeVideoByIdList(strings);
    }

    @Override
    public void removeVideoByCourseId(String courseId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("course_id",courseId);

        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        List<String> strings = new ArrayList<>();
        maps.stream().forEach(map -> {
            String videoSourceId = map.get("video_source_id").toString();
            strings.add(videoSourceId);
        });
        vodVideoService.removeVideoByIdList(strings);
    }
}
