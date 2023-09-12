package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface EntityMapper <D, E> {
    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget E entity, D dto);

    JoularEntity toEntity(JoularEntityDTO joularEntityDTO);
}
