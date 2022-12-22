package io.shelang.aghab.service.mapper;

import io.shelang.aghab.domain.LinkUser;
import io.shelang.aghab.service.dto.link.LinkUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "cdi",
    uses = {LinkUser.LinkUserId.class})
public interface LinkUserMapper extends EntityMapper<LinkUserDTO, LinkUser> {

  @Mapping(target = "userId", source = "id.userId")
  @Mapping(target = "linkId", source = "linkId")
  @Mapping(target = "linkHash", source = "id.linkHash")
  @Mapping(target = "createAt", source = "createAt")
  LinkUserDTO toDTO(LinkUser linkUser);

  @Mapping(target = "id.userId", source = "userId")
  @Mapping(target = "linkId", source = "linkId")
  @Mapping(target = "id.linkHash", source = "linkHash")
  @Mapping(target = "createAt", source = "createAt")
  LinkUser toEntity(LinkUserDTO linkUser);
}
