package io.shelang.aghab.service.dto;

public class RedirectDTO {

  private long id;
  private String url;
  private short statusCode;

  public RedirectDTO() {}

  public long getId() {
    return id;
  }

  public RedirectDTO setId(long id) {
    this.id = id;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public RedirectDTO setUrl(String url) {
    this.url = url;
    return this;
  }

  public short getStatusCode() {
    return statusCode;
  }

  public RedirectDTO setStatusCode(short statusCode) {
    this.statusCode = statusCode;
    return this;
  }
}
