package io.shelang.aghab.service.uaa;

import nl.basjes.parse.useragent.UserAgent;

public enum UserAgentField {
  OPERATING_SYSTEM_CLASS(UserAgent.OPERATING_SYSTEM_CLASS),
  OPERATING_SYSTEM_NAME(UserAgent.OPERATING_SYSTEM_NAME),
  OPERATING_SYSTEM_VERSION(UserAgent.OPERATING_SYSTEM_VERSION),
  DEVICE_CLASS(UserAgent.DEVICE_CLASS),
  DEVICE_NAME(UserAgent.DEVICE_NAME),
  DEVICE_BRAND(UserAgent.DEVICE_BRAND),
  AGENT_CLASS(UserAgent.AGENT_CLASS),
  AGENT_NAME(UserAgent.AGENT_NAME),
  AGENT_VERSION(UserAgent.AGENT_VERSION);

  private final String fieldName;

  UserAgentField(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
