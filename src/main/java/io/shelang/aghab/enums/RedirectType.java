package io.shelang.aghab.enums;

public enum RedirectType {
  REDIRECT,
  SCRIPT,
  IFRAME;

  public static RedirectType from(String type) {
    try {
      return RedirectType.valueOf(type.toUpperCase());
    } catch (NullPointerException | IllegalArgumentException ex) {
      return RedirectType.REDIRECT;
    }
  }
}
