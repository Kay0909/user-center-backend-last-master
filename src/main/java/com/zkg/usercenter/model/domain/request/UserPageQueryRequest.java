package com.zkg.usercenter.model.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户分页查询请求体
 */
@Data
public class UserPageQueryRequest {

    /**
     * 页码，从1开始
     */
    @Schema(description = "页码，从1开始", example = "1", defaultValue = "1")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "10", defaultValue = "10")
    private Integer pageSize = 10;

    /**
     * 用户账号（模糊搜索）
     */
    @Schema(description = "用户账号（支持模糊搜索）", example = "user123")
    private String userAccount;

    /**
     * 性别，0-女，1-男
     */
    @Schema(description = "性别，0-女，1-男", example = "1")
    private Integer gender;

    /**
     * 用户昵称（模糊搜索）
     */
    @Schema(description = "用户昵称（支持模糊搜索）", example = "小明")
    private String username;
}
