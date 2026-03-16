package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.service.dto.ConstructorContextDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ConstructorContextEntityMapperTest {
    private ConstructorContextEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ConstructorContextEntityMapper.class);
    }

    @Test
    void shouldMapStackTraceElementTest() {
        StackTraceElement element =
            new StackTraceElement("com.example.MyClass", "myMethod", "MyClass.java", 42);
        StackTraceElementDTO dto = mapper.map(element);

        assertThat(dto).isNotNull();
        assertThat(dto.getClassName()).isEqualTo("com.example.MyClass");
        assertThat(dto.getMethodName()).isEqualTo("myMethod");
        assertThat(dto.getFileName()).isEqualTo("MyClass.java");
        assertThat(dto.getLineNumber()).isEqualTo(42);
    }

    @Test
    void shouldReturnNullWhenStackTraceElementIsNullTest() {
        StackTraceElementDTO dto = mapper.map(null);
        assertThat(dto).isNull();
    }

    @Test
    void shouldMapConstructorContextDtoToEntityTest() {
        ConstructorContextDTO dto = new ConstructorContextDTO("MyClass.java", "com.example.MyClass", "myMethod", List.of("int, String"), List.of(), List.of(), "snapshot");

        ConstructorContextEntity entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getFileName()).isEqualTo("MyClass.java");
        assertThat(entity.getClassName()).isEqualTo("com.example.MyClass");
        assertThat(entity.getMethodName()).isEqualTo("myMethod");
        assertThat(entity.getParameters()).isEqualTo(List.of("int, String"));
    }
}
