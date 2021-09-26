package io.shelang.aghab.service.uaa;

import io.shelang.aghab.enums.AlternativeLinkType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class UserAgentAnalyzer {

  private UserAgentAnalyzer() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public static List<String> detectType(String ua) {
    if (ua == null || ua.isEmpty() || ua.isBlank()) return Collections.emptyList();

    ua = ua.toLowerCase();

    if (ua.contains("android"))
      return Arrays.asList(AlternativeLinkType.MOBILE.name(), AlternativeLinkType.ANDROID.name());
    else if (ua.contains("iphone") || ua.contains("ios"))
      return Arrays.asList(AlternativeLinkType.MOBILE.name(), AlternativeLinkType.IOS.name());
    else if (ua.contains("mobile")
        || ua.contains("phone")
        || ua.contains("ipad")
        || ua.contains("tablet")) return List.of(AlternativeLinkType.MOBILE.name());
    else if (ua.contains("windows") || ua.contains("macintosh") || ua.contains("linux"))
      return List.of(AlternativeLinkType.DESKTOP.name());

    return Collections.emptyList();
  }
}
