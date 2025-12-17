package com.zkg.usercenter.model.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 3, max = 10, message = "用户账号长度应在3到10之间")
    @javax.validation.constraints.Pattern(
            regexp = "^[^`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$",
            message = "用户账号不能包含特殊字符"
    )
    @Schema(description = "用户账号")
    private String userAccount;

    /**
     * 用户密码
     * 需要为字母和数字两种组合方式
     */
    @javax.validation.constraints.Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
            message = "用户密码必须包含字母和数字的组合"
    )
    @NotBlank(message = "用户密码不能为空")
    @Size(min = 6, max = 20, message = "用户密码长度应在6到20之间")
    @Schema(description = "用户密码")
    private String userPassword;

    /**
     * 校验密码
     */
    @NotBlank(message = "校验密码不能为空")
    @Size(min = 6, max = 20, message = "校验密码长度应在6到20之间")
    @Schema(description = "校验密码")
    private String checkPassword;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 3, max = 10, message = "用户昵称长度应在3到10之间")
    @Schema(description = "用户昵称")
    private String username;

    /**
     * 性别
     */
    @Schema(description = "性别")
    @NotBlank(message = "性别不能为空")
    private Integer gender;
}
