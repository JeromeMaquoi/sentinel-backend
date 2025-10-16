package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.repository.AstElemEntityRepository;
import com.snail.sentinel.backend.service.dto.AstElemEntityDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StackTraceEnrichmentServiceImplTest {
    @Mock
    private AstElemEntityRepository astElemEntityRepository;

    @InjectMocks
    private StackTraceEnrichmentServiceImpl service;

    private StackTraceElement stackTraceElement;

    @BeforeEach
    void setUp() {
        stackTraceElement = new StackTraceElement("com.example.MyClass", "testMethod", "testClass.java", 1);
    }

    @Test
    void enrichStackTraceShouldReturnDTOWhenAstElemFoundTest() {
        List<String> expectedParams = List.of("param1", "param2");
        AstElemEntityDTO astElem = new AstElemEntityDTO();
        astElem.setParameters(expectedParams);

        when(astElemEntityRepository.findMatchingAstElem("testClass.java", "com.example.MyClass", "testMethod", 1)).thenReturn(Optional.of(astElem));

        List<StackTraceElementDTO> result = service.enrichStackTrace(List.of(stackTraceElement));

        assertThat(result).hasSize(1);
        StackTraceElementDTO dto = result.get(0);

        assertThat(dto.getFileName()).isEqualTo("testClass.java");
        assertThat(dto.getMethodName()).isEqualTo("testMethod");
        assertThat(dto.getClassName()).isEqualTo("com.example.MyClass");
        assertThat(dto.getLineNumber()).isEqualTo("1");
        assertThat(dto.getFileName()).isEqualTo("testClass.java");
        assertThat(dto.getParameters()).containsExactly("param1", "param2");
    }
}
