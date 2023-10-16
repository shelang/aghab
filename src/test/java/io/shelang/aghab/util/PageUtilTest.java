package io.shelang.aghab.util;

import io.quarkus.panache.common.Page;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageUtilTest {

    @Test
    void getCorrectPageWithNegativeNumbers() {
        Page page1 = PageUtil.of(-1, -10);
        Page page2 = PageUtil.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
        assertEquals(0, page1.index);
        assertEquals(10, page1.size);
        assertEquals(0, page2.index);
        assertEquals(10, page2.size);

    }

    @Test
    void getCorrectPageWithZero() {
        Page page = PageUtil.of(0, 0);
        assertEquals(0, page.index);
        assertEquals(10, page.size);
    }

    @Test
    void getSize50InCaseRequestingMoreThan50() {
        Page page1 = PageUtil.of(1, 51);
        Page page2 = PageUtil.of(1, Integer.MAX_VALUE);
        assertEquals(50, page1.size);
        assertEquals(50, page2.size);
    }

    @Test
    void getValidPageWithNullValue() {
        Page page = PageUtil.of(null, 10);
        assertEquals(0, page.index);
        assertEquals(10, page.size);

        page = PageUtil.of(1, null);
        assertEquals(0, page.index);
        assertEquals(10, page.size);

        page = PageUtil.of(null, null);
        assertEquals(0, page.index);
        assertEquals(10, page.size);
    }

}