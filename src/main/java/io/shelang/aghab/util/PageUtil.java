package io.shelang.aghab.util;

import io.quarkus.panache.common.Page;

public final class PageUtil {

  private PageUtil() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public static Page of(int page, int size) {
    page = NumberUtil.normalizeValue(page, 1) - 1;
    size = NumberUtil.normalizeValue(size, 10);

    if (size > 50) {
      size = 50;
    }

    return Page.of(page, size);
  }

}
