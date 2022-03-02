package com.stitch.service.edu.service;

import com.stitch.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stitch.service.edu.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
public interface IChapterService extends IService<Chapter> {

    boolean removeChapterById(String id);

    List<ChapterVo> nestedList(String courseId);
}
