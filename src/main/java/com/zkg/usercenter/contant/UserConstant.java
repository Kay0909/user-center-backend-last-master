package com.zkg.usercenter.contant;

/**
 * 用户常量
 *
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    //  ------- 权限 --------

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;


    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

    
    /**
     * 默认未删除状态: 0
     */
    int DEFAULT_DELETE_STATUS = 0;


    /**
     * 删除状态：1  已删除
     */
    int DEL_DELETE_STATUS = 1;

        
    /**
     * 默认未禁用状态: 0 未禁用
     */
    int DEFAULT_DISABLE_STATUS = 0;


    /**
     * 禁用状态：1  禁用
     */
    int DISABLE_STATUS = 1;

}
