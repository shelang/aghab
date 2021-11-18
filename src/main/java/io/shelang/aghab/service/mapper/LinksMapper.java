package io.shelang.aghab.service.mapper;

import io.shelang.aghab.domain.Link;
import io.shelang.aghab.domain.LinkAlternative;
import io.shelang.aghab.enums.AlternativeLinkType;
import io.shelang.aghab.service.dto.LinkAlternativeDTO;
import io.shelang.aghab.service.dto.LinkDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mapper(
    componentModel = "cdi",
    uses = {LinkMetaMapper.class})
public interface LinksMapper extends EntityMapper<LinkDTO, Link> {

  @Override
  @Mapping(
      target = "os",
      expression =
          "java( toOs( linkAlternativeSetToLinkAlternativeDTOSet( entity.getAlternatives() ) ) )")
  @Mapping(
      target = "devices",
      expression =
          "java( toDevices( linkAlternativeSetToLinkAlternativeDTOSet( entity.getAlternatives() ) ) )")
  @Mapping(target = "alternatives", ignore = true)
  @Mapping(target = "title", source = "linkMeta.title")
  @Mapping(target = "description", source = "linkMeta.description")
  @Mapping(target = "expireAt", source = "linkExpiration.expireAt")
  LinkDTO toDTO(Link entity);

  default Set<LinkAlternativeDTO> toOs(Set<LinkAlternativeDTO> alternatives) {
    if (Objects.isNull(alternatives) || alternatives.isEmpty()) return Collections.emptySet();
    var os = new HashSet<LinkAlternativeDTO>();
    for (var alt : alternatives) {
      AlternativeLinkType altType = AlternativeLinkType.valueOf(alt.getKey().toUpperCase());
      if (AlternativeLinkType.ANDROID.equals(altType) || AlternativeLinkType.IOS.equals(altType))
        os.add(alt);
    }
    return os;
  }

  default Set<LinkAlternativeDTO> toDevices(Set<LinkAlternativeDTO> alternatives) {
    if (Objects.isNull(alternatives) || alternatives.isEmpty()) return Collections.emptySet();
    var devices = new HashSet<LinkAlternativeDTO>();
    for (var alt : alternatives) {
      AlternativeLinkType altType = AlternativeLinkType.valueOf(alt.getKey().toUpperCase());
      if (AlternativeLinkType.MOBILE.equals(altType)
          || AlternativeLinkType.DESKTOP.equals(altType)
          || AlternativeLinkType.TABLET.equals(altType)) devices.add(alt);
    }
    return devices;
  }

  default LinkAlternativeDTO linkAlternativeToLinkAlternativeDTO(LinkAlternative linkAlternative) {
    if (linkAlternative == null) {
      return null;
    }

    LinkAlternativeDTO linkAlternativeDTO = new LinkAlternativeDTO();

    linkAlternativeDTO.setKey(linkAlternative.getKey());
    linkAlternativeDTO.setUrl(linkAlternative.getUrl());

    return linkAlternativeDTO;
  }

  default Set<LinkAlternativeDTO> linkAlternativeSetToLinkAlternativeDTOSet(
      Set<LinkAlternative> set) {
    if (set == null) {
      return null;
    }

    Set<LinkAlternativeDTO> set1 =
        new HashSet<LinkAlternativeDTO>(Math.max((int) (set.size() / .75f) + 1, 16));
    for (LinkAlternative linkAlternative : set) {
      set1.add(linkAlternativeToLinkAlternativeDTO(linkAlternative));
    }

    return set1;
  }
}
