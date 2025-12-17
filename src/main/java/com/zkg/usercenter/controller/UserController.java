package com.zkg.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zkg.usercenter.common.BaseResponse;
import com.zkg.usercenter.common.ErrorCode;
import com.zkg.usercenter.common.ResultUtils;
import com.zkg.usercenter.exception.BusinessException;
import com.zkg.usercenter.model.domain.User;
import com.zkg.usercenter.model.domain.request.UserLoginRequest;
import com.zkg.usercenter.model.domain.request.UserRegisterRequest;
import com.zkg.usercenter.model.domain.request.UserUpdateRequest;
import com.zkg.usercenter.service.RequestToEntity;
import com.zkg.usercenter.service.UserService;
import com.zkg.usercenter.utils.UniqueIdentifierGenerator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.zkg.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.zkg.usercenter.contant.UserConstant.USER_LOGIN_STATE;

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
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody @Schema(description = "注册请求体") UserRegisterRequest userRegisterRequest) {
        // 校验
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody @Schema(description = "登录请求体") UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
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
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(@Schema(description = "用户账号") String userAccount, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userAccount)) {
            queryWrapper.like("userAccount", userAccount);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody @Schema(description = "该条数据id") Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }


    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 根据id更新用户信息
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<User> userUpdate(@RequestBody @Schema(description = "更新请求体") UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User requestToUser = requestToEntity.UpdateRequestToUser(userUpdateRequest);
        User user = userService.userUpdateById(requestToUser);
        return ResultUtils.success(user);
    }

}
