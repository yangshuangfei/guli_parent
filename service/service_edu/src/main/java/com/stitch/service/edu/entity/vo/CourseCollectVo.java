package com.stitch.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseCollectVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String courseId;//课程ID
    private String title;//课程标题
    private BigDecimal price;//课程价格
    private String teacherName;
    private Integer classCount ;
    private String cover;
    private String gmtCreate;


}
