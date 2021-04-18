package io.shelang.aghab.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LinksDTO {

  private Long id;
  private String hash;
  private String alias;
  private String url;
  private Integer status;
  private LinkMetaDTO linkMetaDTO;

  public Long getId() {
    return id;
  }

  public LinksDTO setId(Long id) {
    this.id = id;
    return this;
  }

  public String getHash() {
    return hash;
  }

  public LinksDTO setHash(String hash) {
    this.hash = hash;
    return this;
  }

  public String getAlias() {
    return alias;
  }

  public LinksDTO setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public LinksDTO setUrl(String url) {
    this.url = url;
    return this;
  }

  public Integer getStatus() {
    return status;
  }

  public LinksDTO setStatus(Integer status) {
    this.status = status;
    return this;
  }

  public LinkMetaDTO getLinkMetaDTO() {
    return linkMetaDTO;
  }

  public LinksDTO setLinkMetaDTO(LinkMetaDTO linkMetaDTO) {
    this.linkMetaDTO = linkMetaDTO;
    return this;
  }
}
