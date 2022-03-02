package com.stitch.service.base.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class MemberDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String mobile;
    private String nickname;
}
