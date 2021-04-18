package io.shelang.aghab.service.dto;

import java.time.Instant;

public class LinkMetaDTO {

  private Long id;
  private String title;
  private String description;
  private Instant createAt;
  private Instant updateAt;

  public Long getId() {
    return id;
  }

  public LinkMetaDTO setId(Long id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public LinkMetaDTO setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public LinkMetaDTO setDescription(String description) {
    this.description = description;
    return this;
  }

  public Instant getCreateAt() {
    return createAt;
  }

  public LinkMetaDTO setCreateAt(Instant createAt) {
    this.createAt = createAt;
    return this;
  }

  public Instant getUpdateAt() {
    return updateAt;
  }

  public LinkMetaDTO setUpdateAt(Instant updateAt) {
    this.updateAt = updateAt;
    return this;
  }
}
