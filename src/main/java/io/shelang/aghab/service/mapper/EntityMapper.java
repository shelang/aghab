package io.shelang.aghab.service.mapper;

import java.util.List;
import java.util.Set;

public interface EntityMapper<D, E> {

  public E toEntity(D dto);

  public D toDTO(E entity);

  public List<E> toEntity(List<D> dtoList);

  public List<D> toDTO(List<E> entityList);

  public Set<E> toEntity(Set<D> dtoList);

  public Set<D> toDTO(Set<E> entityList);

}
