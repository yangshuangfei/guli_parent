package com.stitch.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stitch.common.base.result.ResultCodeEnum;
import com.stitch.common.base.util.FormUtils;
import com.stitch.common.base.util.JwtInfo;
import com.stitch.common.base.util.JwtUtils;
import com.stitch.common.base.util.MD5;
import com.stitch.service.base.dto.MemberDto;
import com.stitch.service.base.exception.StitchException;
import com.stitch.service.ucenter.entity.Member;
import com.stitch.service.ucenter.entity.vo.LoginVo;
import com.stitch.service.ucenter.entity.vo.RegisterVo;
import com.stitch.service.ucenter.mapper.MemberMapper;
import com.stitch.service.ucenter.service.IMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2021-03-09
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void register(RegisterVo registerVo) {
        //校验参数
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String passwored = registerVo.getPassword();
        String code = registerVo.getCode();
        System.out.println(nickname + mobile + passwored + code);

        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            throw new StitchException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(passwored) || StringUtils.isEmpty(code)) {
            throw new StitchException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验验证码
        String checkCode = (String) redisTemplate.opsForValue().get(mobile);
        if (!code.equals(checkCode)) {
            throw new StitchException(ResultCodeEnum.CODE_ERROR);
        }

        //用户是否注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Integer integer = baseMapper.selectCount(queryWrapper);
        if (integer > 0) {
            throw new StitchException(ResultCodeEnum.REGISTER_MOBLE_ERROR);//用户已注册
        }

        //注册
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(passwored));
        member.setAvatar("https://file-20201228-1.oss-cn-shenzhen.aliyuncs.com/default_colleagues.jpg");
        member.setIsDisabled(false);
        baseMapper.insert(member);
    }

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验,参数是否合法
        //校验手机号码
        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile) || StringUtils.isEmpty(password)) {
            throw new StitchException(ResultCodeEnum.PARAM_ERROR);
        }

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Member member = baseMapper.selectOne(queryWrapper);
        if (member == null) {
            throw new StitchException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        //校验密码是否正确
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new StitchException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //校验用户是否可以登录
        if (member.getIsDisabled()) {
            throw new StitchException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //登录，生成token

        JwtInfo info = new JwtInfo();
        info.setId(member.getId());
        info.setNickname(member.getNickname());
        info.setAvatar(member.getAvatar());

        String token = JwtUtils.getJwtToken(info, 1800);
        return token;
    }

    @Override
    public Member getByOpenid(String openid) {
        if (org.springframework.util.StringUtils.isEmpty(openid)) {
//            throw new StitchException(ResultCodeEnum.);
        }
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openid);
        Member member = baseMapper.selectOne(queryWrapper);
        if (member != null) {

        }

        return member;
    }

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        Member member = baseMapper.selectById(memberId);
        MemberDto memberDto = new MemberDto();
        BeanUtils.copyProperties(member,memberDto);
        return memberDto;
    }

    /**
     * 统计某一天注册用户，
     * @param day
     * @return
     */
    @Override
    public Integer selectRegisterNumByDay(String day) {

        return baseMapper.selectRegisterNumByDay(day);
    }
}
