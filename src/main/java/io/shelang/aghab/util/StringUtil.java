package io.shelang.aghab.util;

public final class StringUtil {

  private StringUtil() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public static boolean isNullOrEmpty(String s) {
    return s == null || s.equals("");
  }

  public static boolean nonNullOrEmpty(String s) {
    return !isNullOrEmpty(s);
  }

}
