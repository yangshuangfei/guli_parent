package com.stitch.service.edu.service.impl;

import com.stitch.service.edu.entity.Comment;
import com.stitch.service.edu.mapper.CommentMapper;
import com.stitch.service.edu.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2020-12-20
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
