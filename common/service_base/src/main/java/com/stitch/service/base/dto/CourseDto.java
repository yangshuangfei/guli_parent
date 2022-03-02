package com.stitch.service.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private BigDecimal price;
    private String cover;
    private String teacherName;

}
