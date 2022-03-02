package com.stitch.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stitch.service.edu.entity.Chapter;
import com.stitch.service.edu.entity.Video;
import com.stitch.service.edu.entity.vo.ChapterVo;
import com.stitch.service.edu.entity.vo.VideoVo;
import com.stitch.service.edu.mapper.ChapterMapper;
import com.stitch.service.edu.mapper.VideoMapper;
import com.stitch.service.edu.service.IChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements IChapterService {
    @Autowired
    VideoMapper videoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeChapterById(String id) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id", id);
        videoMapper.delete(videoQueryWrapper);

        return this.removeById(id);
    }

    @Override
    public List<ChapterVo> nestedList(String courseId) {
        //方案一：通过courseId获取章节信息，得到List<Chapter>，然后遍历，但是效率不高
        //方案二：通过courseId获取章节信息，得到List<Chapter>，然后再通过course_id获取Video信息List<Video>,选用它

        //获取章节信息列表
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        chapterQueryWrapper.orderByAsc("sort", "id");
        List<Chapter> chapters = baseMapper.selectList(chapterQueryWrapper);

        //获取video信息列表
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        videoQueryWrapper.orderByAsc("sort", "id");
        List<Video> videos = videoMapper.selectList(videoQueryWrapper);

        List<ChapterVo> chapterVoList = new ArrayList<>();
        for (int i = 0; i < chapters.size(); i++) {
            ChapterVo chapterVo = new ChapterVo();
            Chapter chapter = chapters.get(i);
            BeanUtils.copyProperties(chapter, chapterVo);
            chapterVoList.add(chapterVo);
            System.out.println(i);
            if (videos.isEmpty()) {
                continue;
            }
            List<VideoVo> videoVos = new ArrayList<>();
            for (int j = 0; j < videos.size(); j++) {
                if (chapter.getId().equals(videos.get(j).getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    videoVo.setId(videos.get(j).getId());
                    videoVo.setFree(videos.get(j).getIsFree() == 0 ? true : false);
                    videoVo.setSort(videos.get(j).getSort());
                    videoVo.setTitle(videos.get(j).getTitle());
                    videoVo.setVideoSourceId(videos.get(j).getVideoSourceId());
                    videoVos.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVos);
        }
        System.out.println(chapterVoList.size());
        return chapterVoList;
    }
}
