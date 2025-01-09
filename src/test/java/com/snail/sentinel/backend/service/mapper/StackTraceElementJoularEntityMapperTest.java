package com.snail.sentinel.backend.service.mapper;

import static com.snail.sentinel.backend.domain.StackTraceElementJoularEntityAsserts.*;
import static com.snail.sentinel.backend.domain.StackTraceElementJoularEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StackTraceElementJoularEntityMapperTest {

    private StackTraceElementJoularEntityMapper stackTraceElementJoularEntityMapper;

    @BeforeEach
    void setUp() {
        stackTraceElementJoularEntityMapper = new StackTraceElementJoularEntityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStackTraceElementJoularEntitySample1();
        var actual = stackTraceElementJoularEntityMapper.toEntity(stackTraceElementJoularEntityMapper.toDto(expected));
        assertStackTraceElementJoularEntityAllPropertiesEquals(expected, actual);
    }
}
