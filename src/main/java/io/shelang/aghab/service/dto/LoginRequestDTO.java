package io.shelang.aghab.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

public class LoginRequestDTO {

  @NotBlank
  @Max(200)
  private String username;

  @NotBlank
  @Max(64)
  private String password;

  public String getUsername() {
    return username;
  }

  public LoginRequestDTO setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public LoginRequestDTO setPassword(String password) {
    this.password = password;
    return this;
  }
}
