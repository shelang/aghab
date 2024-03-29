package io.shelang.aghab.service.dto.link;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LinkAlternativeTypesDTO {

  private List<String> os;
  private List<String> devices;
}
