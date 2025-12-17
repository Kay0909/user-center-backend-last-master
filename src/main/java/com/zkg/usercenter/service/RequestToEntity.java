package com.zkg.usercenter.service;


import com.zkg.usercenter.model.domain.User;
import com.zkg.usercenter.model.domain.request.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * 请求转为对应的实体
 * @author kay
 */
@Component
@Mapper(componentModel = "spring")
public interface RequestToEntity {

    RequestToEntity INSTANCE = Mappers.getMapper(RequestToEntity.class);
    // 定义映射方法
    User UpdateRequestToUser(UserUpdateRequest request);
}
