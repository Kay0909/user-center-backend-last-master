package com.zkg.usercenter.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     */
    @Schema(description = "状态码:0:ok;40000:请求参数错误;40001:请求数据为空;40100:未登录;40101:无权限;50000:系统内部异常")
    private int code;

    /**
     * 数据
     */
    @Schema(description = "数据")
    private T data;

    /**
     * 消息
     */
    @Schema(description = "消息")
    private String message;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
