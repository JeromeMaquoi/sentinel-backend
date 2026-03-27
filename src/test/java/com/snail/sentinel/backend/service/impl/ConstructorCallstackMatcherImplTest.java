package com.snail.sentinel.backend.service.impl;
import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.repository.ConstructorContextEntityRepository;
import com.snail.sentinel.backend.service.dto.MatchedConstructorDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ConstructorCallstackMatcherImplTest {
    @Mock
    private ConstructorContextEntityRepository repository;
    private ConstructorCallstackMatcherImpl matcher;
    @BeforeEach
    void setUp() {
        matcher = new ConstructorCallstackMatcherImpl(repository);
    }
    @Test
    void findMatchingConstructorsWithNullCallstack() {
        AggregatedRuntimeCallTreeMeasurementDTO measurement = new AggregatedRuntimeCallTreeMeasurementDTO();
        measurement.setCallstack(null);
        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(measurement);
        assertThat(result).isEmpty();
        verify(repository, never()).findByClassName(anyString());
    }
    @Test
    void findMatchingConstructorsWithEmptyCallstack() {
        List<String> emptyCallstack = List.of();
        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(emptyCallstack);
        assertThat(result).isEmpty();
        verify(repository, never()).findByClassName(anyString());
    }
    @Test
    void findMatchingConstructorsWithSuccessfulMatch() {
        List<String> callstack = List.of(
            "org.apache.commons.lang3.CharRange.<init>",
            "org.apache.commons.lang3.CharRange.is",
            "org.apache.commons.lang3.CharRangeTest.testContainsNullArg"
        );
        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("123");
        constructor.setClassName("org.apache.commons.lang3.CharRange");
        constructor.setMethodName("<init>");
        constructor.setParameters(List.of("char", "char", "boolean"));
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "<init>", 269),
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "is", 163),
            new StackTraceElementDTO("CharRangeTest.java", "org.apache.commons.lang3.CharRangeTest",
                "testContainsNullArg", 265)
        ));
        when(repository.findByClassName("org.apache.commons.lang3.CharRange"))
            .thenReturn(List.of(constructor));
        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCallstackPosition()).isZero();
        assertThat(result.get(0).getConstructor()).isEqualTo(constructor);
    }
}
