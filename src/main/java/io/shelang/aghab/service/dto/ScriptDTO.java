package io.shelang.aghab.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ScriptDTO {

    private Long id;
    private String name;
    private Integer timeout;
    private String title;
    private String content;

}
