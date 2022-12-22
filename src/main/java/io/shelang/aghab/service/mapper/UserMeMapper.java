package io.shelang.aghab.service.mapper;

import io.shelang.aghab.domain.User;
import io.shelang.aghab.service.dto.auth.UserMeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface UserMeMapper extends EntityMapper<UserMeDTO, User> {

}
