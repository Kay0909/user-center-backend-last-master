package com.zkg.usercenter.service;

import com.zkg.usercenter.common.PageResponse;
import com.zkg.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zkg.usercenter.model.domain.request.UserLoginRequest;
import com.zkg.usercenter.model.domain.request.UserPageQueryRequest;
import com.zkg.usercenter.model.domain.request.UserRegisterRequest;
import com.zkg.usercenter.model.domain.request.UserUpdateRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户信息
     * @return 新用户 id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @param request          请求
     * @return 脱敏后的用户信息
     */
    User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 用户更新
     *
     * @param requestToUser
     * @return 脱敏后的用户信息
     */
    User userUpdateById(User requestToUser);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据用户账号查询用户信息
     *
     * @param userAccount 用户账号
     * @param request     请求
     * @return 用户信息列表
     */
    List<User> searchUser(String userAccount, HttpServletRequest request);

    /**
     * 根据用户id删除用户
     *
     * @param id      用户id
     * @param request 请求
     * @return 响应结果
     */
    boolean deleteUser(Long id, HttpServletRequest request);

    /**
     * 获取当前用户
     *
     * @param request 请求
     * @return 当前用户
     */
    User getCurrentUser(HttpServletRequest request);

    /**
     * 根据id更新用户信息
     *
     * @param userUpdateRequest 更新请求体
     * @return 响应结果
     */
    boolean updateUserById(UserUpdateRequest userUpdateRequest);

    /**
     * 根据id查询用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    User getUserById(Long id);

    /**
     * 根据分页查询用户信息
     *
     * @param pageQuery 分页查询请求体
     * @param request   请求
     * @return 分页用户信息
     */
    PageResponse<User> searchUserByPage(UserPageQueryRequest pageQuery, HttpServletRequest request);
}
