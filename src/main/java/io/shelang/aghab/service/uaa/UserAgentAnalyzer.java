package io.shelang.aghab.service.uaa;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nl.basjes.parse.useragent.UserAgent;

public final class UserAgentAnalyzer {

  private static final List<String> FIELD_NAMES =
      Arrays.stream(UserAgentField.values())
          .map(UserAgentField::getFieldName)
          .collect(Collectors.toList());

  private static final nl.basjes.parse.useragent.UserAgentAnalyzer uaa =
      nl.basjes.parse.useragent.UserAgentAnalyzer.newBuilder()
          .withFields(FIELD_NAMES)
          .hideMatcherLoadStats()
          .immediateInitialization()
          .withCache(10000)
          .build();

  private UserAgentAnalyzer() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public static List<String> detectType(String ua) {
    UserAgent.ImmutableUserAgent agent = uaa.parse(ua);

    String os = agent.get(UserAgent.OPERATING_SYSTEM_NAME).getValue().toLowerCase();
    String device = agent.get(UserAgent.DEVICE_CLASS).getValue().toLowerCase();

    if (device.equals("phone")) {
      device = "mobile";
    }

    switch (os) {
      case "darwin (ios)":
        os = "ios";
        break;
      case "windows nt":
        os = "windows";
        break;
      case "mac os x":
      case "mac os":
        os = "macos";
        break;
      case "ubuntu":
      case "freebsd":
      case "netbsd":
      case "openbsd":
      case "sunos":
      case "fedora":
        os = "linux";
        break;
      case "windows phone":
        os = "windowsphone";
        break;
      default:
        break;
    }

    return List.of(device.toUpperCase(), os.toUpperCase());
  }

  public static UserAgent.ImmutableUserAgent parse(String ua) {
    return uaa.parse(ua);
  }
}
