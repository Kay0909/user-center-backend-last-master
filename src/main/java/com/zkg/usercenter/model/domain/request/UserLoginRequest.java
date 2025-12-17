package com.zkg.usercenter.model.domain.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户登录请求体
 *  

 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

/**
     /**
     * 用户账号
     */
     @NotBlank(message = "用户账号不能为空")
     @Size(min = 3, max = 10, message = "用户账号长度应在3到10之间")
     @Pattern(
         regexp = "^[^`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$",
         message = "用户账号不能包含特殊字符"
     )
     @Schema(description = "用户账号")
     private String userAccount;
 
     /**
      * 用户密码
      * 需要为字母和数字两种组合方式
      */
     @Pattern(
         regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
         message = "用户密码必须包含字母和数字的组合"
     )
     @NotBlank(message = "用户密码不能为空")
     @Size(min = 6, max = 20, message = "用户密码长度应在6到20之间")
     @Schema(description = "用户密码")
     private String userPassword;

}
