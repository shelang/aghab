package io.shelang.aghab.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilTest {

    @Test
    void getIntegerIfIsNullReturnDefault() {
        assertEquals(1, NumberUtil.normalizeValue(1, 2));
        assertEquals(2, NumberUtil.normalizeValue(null, 2));
    }

}