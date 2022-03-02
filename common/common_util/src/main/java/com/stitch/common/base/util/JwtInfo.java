package com.stitch.common.base.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtInfo {
    private String id;
    private String nickname;
    private String avatar;
}
