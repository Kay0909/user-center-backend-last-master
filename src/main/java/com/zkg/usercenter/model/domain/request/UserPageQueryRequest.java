package com.zkg.usercenter.model.domain.request;

import lombok.Data;

@Data
public class UserPageQueryRequest {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 用户账号（模糊搜索）
     */
    private String userAccount;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户昵称（模糊搜索）
     */
    private String username;
}
