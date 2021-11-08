package io.shelang.aghab.util;

import java.util.Objects;

public final class NumberUtil {

    private NumberUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static int normalizeValue(Integer value, int defaultValue) {
        if (Objects.isNull(value)) {
            value = defaultValue;
        }
        return value;
    }
}
