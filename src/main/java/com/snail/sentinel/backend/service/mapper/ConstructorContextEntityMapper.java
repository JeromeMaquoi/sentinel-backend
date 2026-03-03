package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.service.dto.ConstructorContextDTO;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConstructorContextEntityMapper extends EntityMapper<ConstructorContextEntityDTO, ConstructorContextEntity> {
    ConstructorContextEntityDTO toDto(ConstructorContextDTO entity);

    default StackTraceElementDTO map(StackTraceElement element) {
        if (element == null) {
            return null;
        }
        return new StackTraceElementDTO(
            element.getFileName(),
            element.getClassName(),
            element.getMethodName(),
            element.getLineNumber()
        );
    }
}
