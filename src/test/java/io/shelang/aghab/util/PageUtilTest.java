package io.shelang.aghab.util;

import io.quarkus.panache.common.Page;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageUtilTest {

    @Test
    void getCorrectPageWithNegativeNumbers() {
        Page page1 = PageUtil.of(-1, -10);
        Page page2 = PageUtil.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
        assertEquals(page1.index, 0);
        assertEquals(page1.size, 10);
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
        assertEquals(page1.size, 50);
        assertEquals(page2.size, 50);
    }

}