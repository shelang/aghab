package io.shelang.aghab.enums;

public enum AlternativeLinkDeviceType {
  DESKTOP,
  MOBILE,
  EMBEDDED,
  TABLET,
  WATCH,
  AUGMENTED_REALITY,
  VIRTUAL_REALITY,
  E_READER,
  SET_TOP_BOX,
  TV,
  GAME_CONSOLE,
  HANDHELD_GAME_CONSOLE,
  VOICE,
  CAR,
  ANONYMIZED,
  ROBOT,
  ROBOT_MOBILE,
  ROBOT_IMITATOR,
  HACKER,
  UNKNOWN,
  UNPARSEABLE;

  public static AlternativeLinkDeviceType from(String type) {
    try {
      return AlternativeLinkDeviceType.valueOf(type.toUpperCase());
    } catch (NullPointerException | IllegalArgumentException e) {
      try {
        return AlternativeLinkDeviceType.valueOf(type.toLowerCase());
      } catch (NullPointerException | IllegalArgumentException ex) {
        return UNPARSEABLE;
      }
    }
  }
}
