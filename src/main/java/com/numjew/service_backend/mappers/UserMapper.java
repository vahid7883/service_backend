package com.numjew.service_backend.mappers;

import com.numjew.service_backend.user.UserDto;
import com.numjew.service_backend.user.RegisterUserRequest;
import com.numjew.service_backend.user.UpdateUserRequest;
import com.numjew.service_backend.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
