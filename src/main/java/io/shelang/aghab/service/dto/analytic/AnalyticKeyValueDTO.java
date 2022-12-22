package io.shelang.aghab.service.dto.analytic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AnalyticKeyValueDTO<K, V> {

  private K key;
  private V value;
}
