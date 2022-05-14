package io.shelang.aghab.service.mapper;

import io.shelang.aghab.domain.User;
import io.shelang.aghab.service.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface UserMapper extends EntityMapper<UserDTO, User> {

}
