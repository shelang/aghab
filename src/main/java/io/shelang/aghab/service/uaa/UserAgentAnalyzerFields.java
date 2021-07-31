package io.shelang.aghab.service.uaa;

public enum UserAgentAnalyzerFields {
  OperatingSystemClass("OperatingSystemClass"),
  OperatingSystemName("OperatingSystemName"),
  OperatingSystemVersion("OperatingSystemVersion"),
  DeviceClass("DeviceClass"),
  DeviceName("DeviceName"),
  DeviceBrand("DeviceBrand"),
  AgentClass("AgentClass"),
  AgentName("AgentName"),
  AgentVersion("AgentVersion"),
  ;

  private final String field;

  UserAgentAnalyzerFields(String field) {
    this.field = field;
  }

  public String field() {
    return field;
  }
}
