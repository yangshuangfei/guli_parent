package com.stitch.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录VO（值对象）
 */
@Data
public class LoginVo implements Serializable {
    private static final long serializableUID = 1L;
    private String mobile;
    private String password;
}
