package io.shelang.aghab.service.dto;

public class LoginDTO {

  private String token;
  private String refresh;

  public String getToken() {
    return token;
  }

  public LoginDTO setToken(String token) {
    this.token = token;
    return this;
  }

  public String getRefresh() {
    return refresh;
  }

  public LoginDTO setRefresh(String refresh) {
    this.refresh = refresh;
    return this;
  }
}
