package io.shelang.aghab.model;

public class LinkDTO {
  private Long id;
  private String hash;
  private String url;
  private Integer status;
  private String title;
  private String description;

  public LinkDTO() {}

  public Long getId() {
    return id;
  }

  public LinkDTO setId(Long id) {
    this.id = id;
    return this;
  }

  public String getHash() {
    return hash;
  }

  public LinkDTO setHash(String hash) {
    this.hash = hash;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public LinkDTO setUrl(String url) {
    this.url = url;
    return this;
  }

  public Integer getStatus() {
    return status;
  }

  public LinkDTO setStatus(Integer status) {
    this.status = status;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public LinkDTO setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public LinkDTO setDescription(String description) {
    this.description = description;
    return this;
  }
}
