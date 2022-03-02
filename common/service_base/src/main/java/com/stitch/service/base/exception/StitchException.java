package com.stitch.service.base.exception;

import com.stitch.common.base.result.ResultCodeEnum;
import lombok.Data;

/**
 * 自定义异常，需要定义成运行时异常
 * @author ysf
 * @since 20201229
 */
@Data
public class StitchException extends RuntimeException {
    private Integer code;

    public StitchException(String message, Integer code) {
        super(message);
        this.code = code;
    }
    public StitchException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "StitchException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
