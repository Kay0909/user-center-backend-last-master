package com.zkg.usercenter.model.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号
     */
    @Schema(description = "用户账号")
    private String userAccount;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码")
    private String userPassword;

    /**
     * 校验密码
     */
    @Schema(description = "校验密码")
    private String checkPassword;

    /**
     * 星球编号
     */
    @Schema(description = "星球编号:扩展字段")
    private String planetCode;
}
