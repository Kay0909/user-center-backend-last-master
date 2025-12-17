package com.zkg.usercenter.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分页通用返回类
 *
 * data 直接承载 MyBatis-Plus 的 IPage<T>
 *
 * @param <T> 记录类型
 */
@Schema(description = "分页通用返回类（data 为 IPage<T>）")
public class PageResponse<T> extends BaseResponse<IPage<T>> {

    public PageResponse(int code, IPage<T> data, String message, String description) {
        super(code, data, message, description);
    }

    public PageResponse(int code, IPage<T> data, String message) {
        super(code, data, message);
    }

    public PageResponse(int code, IPage<T> data) {
        super(code, data);
    }

    public PageResponse(ErrorCode errorCode) {
        super(errorCode);
    }
}


