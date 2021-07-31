package io.shelang.aghab.event.dto;

import io.vertx.core.MultiMap;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AnalyticLinkEvent {

    private Long id;
    private String hash;
    private MultiMap headers;

}
