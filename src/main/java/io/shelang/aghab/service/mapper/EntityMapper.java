package io.shelang.aghab.service.mapper;

import java.util.List;
import java.util.Set;

public interface EntityMapper<D, E> {

  E toEntity(D dto);

  D toDTO(E entity);

  List<E> toEntity(List<D> dtoList);

  List<D> toDTO(List<E> entityList);

  Set<E> toEntity(Set<D> dtoList);

  Set<D> toDTO(Set<E> entityList);

}
