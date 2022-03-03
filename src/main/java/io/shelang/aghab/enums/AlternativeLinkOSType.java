package io.shelang.aghab.enums;

public enum AlternativeLinkOSType {
  ANDROID,
  IOS,
  WINDOWS,
  WINDOWSPHONE,
  LINUX,
  MACOS,
  UNKNOWN,
  UNPARSEABLE;

  public static AlternativeLinkOSType from(String type) {
    try {
      return AlternativeLinkOSType.valueOf(type.toUpperCase());
    } catch (NullPointerException | IllegalArgumentException e) {
      try {
        return AlternativeLinkOSType.valueOf(type.toLowerCase());
      } catch (NullPointerException | IllegalArgumentException ex) {
        return UNPARSEABLE;
      }
    }
  }
}
