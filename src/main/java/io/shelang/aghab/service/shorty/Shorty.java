package io.shelang.aghab.service.shorty;

import io.shelang.aghab.exception.InvalidLengthOfHash;

public interface Shorty {
    String generate(int length) throws InvalidLengthOfHash;
}
