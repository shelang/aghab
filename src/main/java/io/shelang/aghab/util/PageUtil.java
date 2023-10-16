package io.shelang.aghab.util;

import io.quarkus.panache.common.Page;

public final class PageUtil {

  private PageUtil() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public static Page of(Integer page, Integer size) {
    page = NumberUtil.normalizeValue(page, 1);
    size = NumberUtil.normalizeValue(size, 10);

    if (size > 50) {
      size = 50;
    }

    if (page < 1) page = 1;
    --page;
    if (size < 1) size = 10;

    return Page.of(page, size);
  }

}
