package io.shelang.aghab.event.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WebhookCallEvent {

    private Long webhookId;
    private Long linkId;
    private String hash;

}
