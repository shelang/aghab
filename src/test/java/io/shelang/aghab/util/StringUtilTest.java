package io.shelang.aghab.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringUtilTest {

  @Test
  void isNullOrEmptyTest() {
    assertTrue(StringUtil.isNullOrEmpty(""));
    //noinspection ConstantConditions
    assertTrue(StringUtil.isNullOrEmpty(null));
    assertFalse(StringUtil.isNullOrEmpty("a"));
    assertFalse(StringUtil.isNullOrEmpty(" "));
  }

  @Test
  void nonNullOrEmptyTest() {
    assertFalse(StringUtil.nonNullOrEmpty(""));
    //noinspection ConstantConditions
    assertFalse(StringUtil.nonNullOrEmpty(null));
    assertTrue(StringUtil.nonNullOrEmpty("a"));
    assertTrue(StringUtil.nonNullOrEmpty(" "));
  }

}