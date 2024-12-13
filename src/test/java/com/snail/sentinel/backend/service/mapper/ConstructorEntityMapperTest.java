package com.snail.sentinel.backend.service.mapper;

import static com.snail.sentinel.backend.domain.ConstructorEntityAsserts.*;
import static com.snail.sentinel.backend.domain.ConstructorEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConstructorEntityMapperTest {

    private ConstructorEntityMapper constructorEntityMapper;

    @BeforeEach
    void setUp() {
        constructorEntityMapper = new ConstructorEntityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConstructorEntitySample1();
        var actual = constructorEntityMapper.toEntity(constructorEntityMapper.toDto(expected));
        assertConstructorEntityAllPropertiesEquals(expected, actual);
    }
}
