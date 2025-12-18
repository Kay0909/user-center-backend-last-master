package com.zkg.usercenter.controller;

import com.zkg.usercenter.common.BaseResponse;
import com.zkg.usercenter.common.ErrorCode;
import com.zkg.usercenter.common.PageResponse;
import com.zkg.usercenter.common.ResultUtils;
import com.zkg.usercenter.exception.BusinessException;
import com.zkg.usercenter.model.domain.User;
import com.zkg.usercenter.model.domain.request.UserLoginRequest;
import com.zkg.usercenter.model.domain.request.UserPageQueryRequest;
import com.zkg.usercenter.model.domain.request.UserRegisterRequest;
import com.zkg.usercenter.model.domain.request.UserUpdateRequest;
import com.zkg.usercenter.service.RequestToEntity;
import com.zkg.usercenter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RequestToEntity requestToEntity;

    /**
     * 分页查询用户
     *
     * @param request   HttpServletRequest
     * @param pageQuery 分页查询请求体
     * @return 用户分页结果
     */
    @Operation(summary = "分页查询用户", description = "根据分页参数查询用户列表")
    @PostMapping("/page")
    public PageResponse<User> searchUserByPage(
            @Parameter(description = "分页查询请求体", required = true)
            @RequestBody UserPageQueryRequest pageQuery,
            @Parameter(hidden = true) HttpServletRequest request) {
        return userService.searchUserByPage(pageQuery, request);
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 响应结果
     */
    @Operation(summary = "用户注册", description = "用户进行注册，返回新用户ID")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(
            @Parameter(description = "注册请求体", required = true)
            @RequestBody UserRegisterRequest userRegisterRequest) {
        long result = userService.userRegister(userRegisterRequest);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求体
     * @param request HttpServletRequest
     * @return
     */
    @Operation(summary = "用户登录", description = "用户进行登录操作")
    @PostMapping("/login")
    public BaseResponse<User> userLogin(
            @Parameter(description = "登录请求体", required = true)
            @RequestBody UserLoginRequest userLoginRequest,
            @Parameter(hidden = true) HttpServletRequest request) {
        User user = userService.userLogin(userLoginRequest, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销（退出登录）
     *
     * @param request 请求
     * @return
     */
    @Operation(summary = "用户注销", description = "用户退出登录操作")
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(
            @Parameter(hidden = true) HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 根据用户账号查询
     *
     * @param userAccount 用户账号
     * @param request     请求
     * @return 用户信息列表
     */
    @Operation(summary = "查询用户", description = "根据用户账号查询用户信息列表")
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(
            @Parameter(description = "用户账号", required = true)
            @RequestParam String userAccount,
            @Parameter(hidden = true) HttpServletRequest request) {
        List<User> list = userService.searchUser(userAccount, request);
        return ResultUtils.success(list);
    }

    /**
     * 根据用户id删除用户
     *
     * @param id      用户id
     * @param request 请求
     * @return 响应结果
     */
    @Operation(summary = "删除用户", description = "根据用户id删除用户")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(
            @Parameter(description = "该条数据id", required = true)
            @RequestBody Long id,
            @Parameter(hidden = true) HttpServletRequest request) {
        boolean b = userService.deleteUser(id, request);
        return ResultUtils.success(b);
    }

    /**
     * 获取当前用户
     *
     * @param request 请求
     * @return 响应结果
     */
    @Operation(summary = "获取当前用户", description = "获取当前登录用户信息")
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(
            @Parameter(hidden = true) HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        return ResultUtils.success(user);
    }

    /**
     * 根据id更新用户信息
     *
     * @param userUpdateRequest 更新请求体
     * @return 响应结果
     */
    @Operation(summary = "更新用户信息", description = "根据id更新用户详细信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> userUpdate(
            @Parameter(description = "更新请求体", required = true)
            @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResultUtils.success(userService.updateUserById(userUpdateRequest));
    }

}
