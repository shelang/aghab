package io.shelang.aghab.exception.mapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ViolationError {
    private String field;
    private String message;
}
