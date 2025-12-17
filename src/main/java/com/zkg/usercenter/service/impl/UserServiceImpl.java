package com.zkg.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkg.usercenter.common.ErrorCode;
import com.zkg.usercenter.common.PageResponse;
import com.zkg.usercenter.contant.UserConstant;
import com.zkg.usercenter.exception.BusinessException;
import com.zkg.usercenter.model.domain.User;
import com.zkg.usercenter.model.domain.request.UserLoginRequest;
import com.zkg.usercenter.model.domain.request.UserPageQueryRequest;
import com.zkg.usercenter.model.domain.request.UserRegisterRequest;
import com.zkg.usercenter.model.domain.request.UserUpdateRequest;
import com.zkg.usercenter.service.UserService;
import com.zkg.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.zkg.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;


    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "zkg";

    /**
     * 用户注册
     *
     * @param request 用户信息
     * @return 新用户 id
     */
    @Override
    public long userRegister(UserRegisterRequest request) {
        // 校验
        if (ObjectUtils.isEmpty(request)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        // 密码和校验密码相同
        if (!request.getUserPassword().equals(request.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不同");
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", request.getUserAccount());
        // 未禁用
        queryWrapper.eq("userStatus", UserConstant.DEFAULT_DISABLE_STATUS);
        // 未删除
        queryWrapper.eq("isDelete", UserConstant.DEFAULT_DELETE_STATUS);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 2. MD5加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + request.getUserPassword()).getBytes());
        // 3. 插入数据
        User user = new User();
        // id采用雪花id
        user.setId(IdWorker.getId());
        user.setUserAccount(request.getUserAccount());
        user.setUsername(request.getUsername());
        user.setGender(request.getGender());
        user.setUserPassword(encryptPassword);
        // 默认用户状态为0
        user.setUserStatus(UserConstant.DEFAULT_DISABLE_STATUS);
        // 默认用户角色为0
        user.setUserRole(UserConstant.DEFAULT_ROLE);
        // 默认用户删除状态为0
        user.setIsDelete(UserConstant.DEFAULT_DELETE_STATUS);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户注册失败");
        }
        // 返回新用户id
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @param request          请求
     * @return 脱敏后的用户信息
     */
    @Override
    public User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 1. 校验
        if (ObjectUtils.isEmpty(userLoginRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userLoginRequest.getUserPassword()).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userLoginRequest.getUserAccount());
        queryWrapper.eq("userPassword", encryptPassword);
        // 未禁用
        queryWrapper.eq("userStatus", UserConstant.DEFAULT_DISABLE_STATUS);
        // 未删除
        queryWrapper.eq("isDelete", UserConstant.DEFAULT_DELETE_STATUS);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 3. 用户脱敏用于处理敏感信息
        User safetyUser = getSafetyUser(user);
        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public User userUpdateById(User requestToUser) {
        userMapper.updateById(requestToUser);
        User afterUpdateUser = userMapper.selectById(requestToUser.getId());
        return afterUpdateUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (ObjectUtils.isEmpty(originUser)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户信息为空");
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        // 表示成功
        return 1;
    }

    /**
     * 根据用户账号查询用户信息
     *
     * @param userAccount 用户账号
     * @param request     请求
     * @return 用户信息列表
     */
    @Override
    public List<User> searchUser(String userAccount, HttpServletRequest request) {
        // 判断是否拥有管理员权限，后续需要优化
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }
        // 查询用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userAccount)) {
            queryWrapper.like("userAccount", userAccount);
            // 未禁用
            queryWrapper.eq("userStatus", UserConstant.DEFAULT_DISABLE_STATUS);
            // 未删除
            queryWrapper.eq("isDelete", UserConstant.DEFAULT_DELETE_STATUS);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        // 返回脱敏后的用户信息列表
        List<User> list = userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
        return list;
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
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    /**
     * 根据用户id删除用户
     *
     * @param id      用户id
     * @param request 请求
     * @return 响应结果
     */
    @Override
    public boolean deleteUser(Long id, HttpServletRequest request) {
        // 判断是否拥有管理员权限，后续需要优化
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不能为空");
        }
        // 查询用户是否存在
        User user = userMapper.selectById(id);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 更新用户删除状态为1
        user.setIsDelete(UserConstant.DEL_DELETE_STATUS);
        int updateResult = userMapper.updateById(user);
        return updateResult > 0;
    }

    /**
     * 获取当前用户
     *
     * @param request 请求
     * @return 当前用户
     */
    @Override
    public User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }
        if (ObjectUtils.isEmpty(currentUser.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不能为空");
        }
        // 查询用户是否存在
        User user = userMapper.selectById(currentUser.getId());
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        return getSafetyUser(user);
    }

    @Override
    public User getUserById(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不能为空");
        }
        // 查询用户是否存在
        User user = userMapper.selectById(id);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        return getSafetyUser(user);
    }

    /**
     * 根据用户id更新用户信息
     *
     * @param userUpdateRequest 更新请求体
     * @return 响应结果
     */
    @Override
    public boolean updateUserById(UserUpdateRequest userUpdateRequest) {
        if (ObjectUtils.isEmpty(userUpdateRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        if (ObjectUtils.isEmpty(userUpdateRequest.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不能为空");
        }
        // 查询用户是否存在
        User user = userMapper.selectById(userUpdateRequest.getId());
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 更新用户信息
        user.setUsername(userUpdateRequest.getUsername());
        user.setAvatarUrl(userUpdateRequest.getAvatarUrl());
        user.setGender(userUpdateRequest.getGender());
        user.setPhone(userUpdateRequest.getPhone());
        user.setEmail(userUpdateRequest.getEmail());
        int updateResult = userMapper.updateById(user);
        return updateResult > 0;
    }

    /**
     * 根据分页查询用户信息
     *
     * @param pageQuery 分页查询请求体
     * @param request   请求
     * @return 分页用户信息
     */
    @Override
    public PageResponse<User> searchUserByPage(UserPageQueryRequest pageQuery, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(pageQuery)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        if (ObjectUtils.isEmpty(pageQuery.getPageNum())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "页码不能为空");
        }
        if (ObjectUtils.isEmpty(pageQuery.getPageSize())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "每页大小不能为空");
        }
        // 查询用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageQuery.getUserAccount())) {
            queryWrapper.like("userAccount", pageQuery.getUserAccount());
        }
        if (StringUtils.isNotBlank(pageQuery.getUsername())) {
            queryWrapper.like("username", pageQuery.getUsername());
        }
        if (ObjectUtils.isNotEmpty(pageQuery.getGender())) {
            queryWrapper.eq("gender", pageQuery.getGender());
        }
        // 未禁用
        queryWrapper.eq("userStatus", UserConstant.DEFAULT_DISABLE_STATUS);
        // 未删除
        queryWrapper.eq("isDelete", UserConstant.DEFAULT_DELETE_STATUS);
        // 分页查询
        Page<User> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        // 查询为空的处理
        if (ObjectUtils.isEmpty(userPage) || ObjectUtils.isEmpty(userPage.getRecords())) {
            return new PageResponse<>(ErrorCode.SUCCESS.getCode(), userPage, "没有查询到用户信息");
        }
        // 返回脱敏后的用户信息列表
        List<User> list = userPage.getRecords().stream().map(this::getSafetyUser).collect(Collectors.toList());
        userPage.setRecords(list);
        return new PageResponse<>(ErrorCode.SUCCESS.getCode(), userPage, "查询成功");
    }
}
