package com.stitch.service.edu.service;

import com.stitch.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
public interface IVideoService extends IService<Video> {
void removeVideoById(String id);
void removeVideoByChapterId(String chapterId);
void removeVideoByCourseId(String courseId);
}
