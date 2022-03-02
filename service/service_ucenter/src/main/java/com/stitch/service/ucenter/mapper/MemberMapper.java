package com.stitch.service.ucenter.mapper;

import com.stitch.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author ysf
 * @since 2021-03-09
 */
@Repository
public interface MemberMapper extends BaseMapper<Member> {
    Integer selectRegisterNumByDay(String day);
}
