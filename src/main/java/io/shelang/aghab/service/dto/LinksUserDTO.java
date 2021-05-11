package io.shelang.aghab.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public class LinksUserDTO {

  private List<LinkUserDTO> links;

  public List<LinkUserDTO> getLinks() {
    return links;
  }

  public LinksUserDTO setLinks(List<LinkUserDTO> links) {
    this.links = links;
    return this;
  }
}
