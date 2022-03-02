package com.stitch.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stitch.common.base.result.R;
import com.stitch.service.base.dto.CourseDto;
import com.stitch.service.edu.entity.*;
import com.stitch.service.edu.entity.form.CourseInfoForm;
import com.stitch.service.edu.entity.vo.*;
import com.stitch.service.edu.feign.OSSFileService;
import com.stitch.service.edu.mapper.*;
import com.stitch.service.edu.service.ICourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CourseCollectMapper courseCollectMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private OSSFileService ossFileService;

    @Transactional(rollbackFor = Exception.class) //事务
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        //1.保存course
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        course.setStatus(Course.COURSE_DRAFT);
        baseMapper.insert(course);

        //2.保存CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);
        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        //根据id获取Course
        Course course = baseMapper.selectById(id);
        if (course == null) {
            return null;
        }
        //根据id获取description
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);
        if (courseDescription == null) {
            return null;
        }

        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    @Transactional(rollbackFor = Exception.class) //事务
    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        //1.更新course
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        course.setStatus(Course.COURSE_DRAFT);
        baseMapper.updateById(course);

        //2.更新CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.updateById(courseDescription);
    }

    @Override
    public IPage<CourseVo> selectPage(Page<CourseVo> courseVoPage, CourseQueryVo courseQueryVo) {
        QueryWrapper<CourseVo> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("c.gmt_create");
        String title = courseQueryVo.getTitle();
        String teacherId = courseQueryVo.getTeacherId();
        String subjectId = courseQueryVo.getSubjectId();
        String subjectParentId = courseQueryVo.getSubjectParentId();
        if (!StringUtils.isEmpty(title)) {
            courseQueryWrapper.like("c.title", title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            courseQueryWrapper.eq("c.teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            courseQueryWrapper.eq("c.subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            courseQueryWrapper.eq("c.subject_id", subjectId);
        }

        //执行查询
        //只需要在mapper层传入封装好的分页组件即可，sql分页条件组装的过程由mp自动完成
        List<CourseVo> courseVoList = baseMapper.selectPageByCourseQueryVo(courseVoPage, courseQueryWrapper);

        //将records设置到pageParam中
        return courseVoPage.setRecords(courseVoList);
    }

    @Override
    public boolean removeCoverById(String id) {
        Course course = baseMapper.selectById(id);
        if (course != null) {
            String cover = course.getCover();
            if (!StringUtils.isEmpty(cover)) {
                R r = ossFileService.removeFile(cover);
                return r.getSuccess();
            }
        }
        return false;
    }


    /**
     * 数据库中外键约束的设置：
     * 互联网分布式项目中不允许使用外键与级联更新，一切设计级联的操作不要依赖数据库层，要在业务层解决
     * <p>
     * 如果业务层解决级联删除功能
     * 那么先删除子表数据，再删除父表数据
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(String id) {
        //根据courseId删除Video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        //根据courseId删除Chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);

        //根据courseId删除Comment
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);

        //根据courseId删除CourseCollect
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id", id);
        courseCollectMapper.delete(courseCollectQueryWrapper);

        //根据courseId删除description
        courseDescriptionMapper.deleteById(id);
        //删除Course
        return this.removeById(id);
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public boolean publishCourseById(String id) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        return this.updateById(course);
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo) {
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("status", Course.COURSE_NORMAL);

        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectParentId())) {
            courseQueryWrapper.eq("subject_parent_id", webCourseQueryVo.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectId())) {
            courseQueryWrapper.eq("subject_id", webCourseQueryVo.getSubjectId());
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getBuyCountSort())) {
            courseQueryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getGmtCreateSort())) {
            courseQueryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getPriceSort())) {
            courseQueryWrapper.orderByAsc("price");
//                courseQueryWrapper.orderByDesc("price");
        }

        return baseMapper.selectList(courseQueryWrapper);
    }

    /**
     * 获取课程信息并更新浏览量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebCourseVo selectWebCourseVoById(String id) {
        Course course = baseMapper.selectById(id);
        //更新浏览数
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);

        //获取课程信息
        return baseMapper.selectWebCourseVoById(id);

    }

    @Cacheable(value = "index", key = "'selectHotCourse'")
    @Override
    public List<Course> selectHotCourse() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CourseDto getCourseDtoByCourseId(String courseId) {
        Course course = baseMapper.selectById(courseId);
        String teacherId = course.getTeacherId();
        Teacher teacher = teacherMapper.selectById(teacherId);
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setCover(course.getCover());
        courseDto.setPrice(course.getPrice());
        courseDto.setTeacherName(teacher.getName());
        courseDto.setTitle(course.getTitle());
        return courseDto;
    }

    @Override
    public void updateBuyCountById(String id) {
        Course course = baseMapper.selectById(id);
        long buyCount = course.getBuyCount() + 1;
        course.setBuyCount(buyCount);
        this.updateById(course);
    }

}
