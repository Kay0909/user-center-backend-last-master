package com.zkg.usercenter.model.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;

/**
 * 用户更新请求体
 *
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * id
     */
    @NotBlank(message = "用户id不能为空")
    @Schema(description = "id")
    private Long id;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String username;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像")
    private String avatarUrl;

    /**
     * 性别
     */
    @Schema(description = "性别")
    private Integer gender;

    /**
     * 电话
     */
    @Schema(description = "电话")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

}
