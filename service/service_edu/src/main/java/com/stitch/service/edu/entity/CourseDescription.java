package com.stitch.service.edu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.stitch.service.base.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程简介
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_course_description")
@ApiModel(value = "CourseDescription对象", description = "课程简介")
public class CourseDescription extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.NONE) //这里会覆盖掉BaseEntity中定义的，因为和Course表是1对1的主键关联，所以不应该有自己的生成策略，和Course一致
    private String id;

    @ApiModelProperty(value = "课程简介")
    private String description;


}
