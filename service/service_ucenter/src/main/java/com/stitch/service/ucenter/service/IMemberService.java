package com.stitch.service.ucenter.service;

import com.stitch.service.base.dto.MemberDto;
import com.stitch.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stitch.service.ucenter.entity.vo.LoginVo;
import com.stitch.service.ucenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author ysf
 * @since 2021-03-09
 */
public interface IMemberService extends IService<Member> {

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    Member getByOpenid(String openid);

    MemberDto getMemberDtoByMemberId(String memberId);

    Integer selectRegisterNumByDay(String day);
}
