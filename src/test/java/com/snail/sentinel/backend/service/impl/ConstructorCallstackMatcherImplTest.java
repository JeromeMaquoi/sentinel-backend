package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.repository.ConstructorContextEntityRepository;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

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

        Map<String, ConstructorContextEntity> result = matcher.findMatchingConstructors(measurement);

        assertThat(result).isEmpty();
        verify(repository, never()).findByClassName(anyString());
    }

    @Test
    void findMatchingConstructorsWithEmptyCallstack() {
        List<String> emptyCallstack = List.of();

        Map<String, ConstructorContextEntity> result = matcher.findMatchingConstructors(emptyCallstack);

        assertThat(result).isEmpty();
        verify(repository, never()).findByClassName(anyString());
    }

    @Test
    void findMatchingConstructorsWithNoConstructorCalls() {
        List<String> callstack = List.of(
            "org.apache.commons.lang3.CharRange.is",
            "org.apache.commons.lang3.CharRangeTest.testContainsNullArg"
        );

        Map<String, ConstructorContextEntity> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).isEmpty();
        verify(repository, never()).findByClassName(anyString());
    }

    @Test
    void findMatchingConstructorsWithConstructorButNoMatches() {
        List<String> callstack = List.of(
            "org.apache.commons.lang3.CharRange.<init>",
            "org.apache.commons.lang3.CharRange.is"
        );

        when(repository.findByClassName("org.apache.commons.lang3.CharRange"))
            .thenReturn(List.of());

        Map<String, ConstructorContextEntity> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).isEmpty();
        verify(repository).findByClassName("org.apache.commons.lang3.CharRange");
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
            new StackTraceElementDTO("CharRangeTest.java", "org.apache.commons.lang3.CharRangeTest", "testContainsNullArg", 265)
        ));

        when(repository.findByClassName("org.apache.commons.lang3.CharRange"))
            .thenReturn(List.of(constructor));

        Map<String, ConstructorContextEntity> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(1);
        assertThat(result).containsKey("org.apache.commons.lang3.CharRange#<init>(char,char,boolean)");
        assertThat(result.get("org.apache.commons.lang3.CharRange#<init>(char,char,boolean)")).isEqualTo(constructor);
    }

    @Test
    void findMatchingConstructorsWithMultipleConstructors() {
        List<String> callstack = List.of(
            "com.example.ClassA.<init>",
            "com.example.ClassB.<init>",
            "com.example.ClassA.methodX",
            "com.example.ClassB.methodY"
        );

        ConstructorContextEntity constructorA = new ConstructorContextEntity();
        constructorA.setId("1");
        constructorA.setClassName("com.example.ClassA");
        constructorA.setMethodName("<init>");
        constructorA.setParameters(List.of());
        constructorA.setStacktrace(List.of(
            new StackTraceElementDTO("ClassA.java", "com.example.ClassA", "<init>", 10)
        ));

        ConstructorContextEntity constructorB = new ConstructorContextEntity();
        constructorB.setId("2");
        constructorB.setClassName("com.example.ClassB");
        constructorB.setMethodName("<init>");
        constructorB.setParameters(List.of("String"));
        constructorB.setStacktrace(List.of(
            new StackTraceElementDTO("ClassB.java", "com.example.ClassB", "<init>", 20)
        ));

        when(repository.findByClassName("com.example.ClassA"))
            .thenReturn(List.of(constructorA));
        when(repository.findByClassName("com.example.ClassB"))
            .thenReturn(List.of(constructorB));

        Map<String, ConstructorContextEntity> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(2);
        assertThat(result).containsKey("com.example.ClassA#<init>()");
        assertThat(result).containsKey("com.example.ClassB#<init>(String)");
    }

    @Test
    void isStacktraceSubsequenceWithNullConstructor() {
        List<String> callstack = List.of("method1", "method2");

        boolean result = matcher.isStacktraceSubsequence(callstack, null);

        assertThat(result).isFalse();
    }

    @Test
    void isStacktraceSubsequenceWithNullStacktrace() {
        List<String> callstack = List.of("method1", "method2");
        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setStacktrace(null);

        boolean result = matcher.isStacktraceSubsequence(callstack, constructor);

        assertThat(result).isFalse();
    }

    @Test
    void isStacktraceSubsequenceWithEmptyStacktrace() {
        List<String> callstack = List.of("method1", "method2");
        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setStacktrace(List.of());

        boolean result = matcher.isStacktraceSubsequence(callstack, constructor);

        assertThat(result).isFalse();
    }

    @Test
    void isStacktraceSubsequenceWithMatchingSequence() {
        List<String> callstack = List.of(
            "org.apache.commons.lang3.CharRange.<init>",
            "org.apache.commons.lang3.CharRange.is",
            "org.apache.commons.lang3.CharRangeTest.testContainsNullArg"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "<init>", 269),
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "is", 163),
            new StackTraceElementDTO("CharRangeTest.java", "org.apache.commons.lang3.CharRangeTest", "testContainsNullArg", 265)
        ));

        boolean result = matcher.isStacktraceSubsequence(callstack, constructor);

        assertThat(result).isTrue();
    }

    @Test
    void isStacktraceSubsequenceWithGapsInSequence() {
        // The runtime callstack might have additional frames not in constructor's stacktrace
        List<String> callstack = List.of(
            "org.apache.commons.lang3.CharRange.<init>",
            "some.intermediate.Class.method",
            "org.apache.commons.lang3.CharRange.is",
            "another.intermediate.Class.method",
            "org.apache.commons.lang3.CharRangeTest.testContainsNullArg"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "<init>", 269),
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "is", 163),
            new StackTraceElementDTO("CharRangeTest.java", "org.apache.commons.lang3.CharRangeTest", "testContainsNullArg", 265)
        ));

        boolean result = matcher.isStacktraceSubsequence(callstack, constructor);

        assertThat(result).isTrue();
    }

    @Test
    void isStacktraceSubsequenceWithNonMatchingSequence() {
        List<String> callstack = List.of(
            "org.apache.commons.lang3.CharRange.is",
            "org.apache.commons.lang3.CharRangeTest.testContainsNullArg"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "<init>", 269),
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "is", 163)
        ));

        boolean result = matcher.isStacktraceSubsequence(callstack, constructor);

        assertThat(result).isFalse();
    }

    @Test
    void isStacktraceSubsequenceWithWrongOrder() {
        List<String> callstack = List.of(
            "org.apache.commons.lang3.CharRange.is",
            "org.apache.commons.lang3.CharRange.<init>",
            "org.apache.commons.lang3.CharRangeTest.testContainsNullArg"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "<init>", 269),
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "is", 163)
        ));

        boolean result = matcher.isStacktraceSubsequence(callstack, constructor);

        assertThat(result).isFalse();
    }

    @Test
    void findMatchingConstructorsIgnoresNullClassNameExtraction() {
        List<String> callstack = List.of(
            "org.apache.commons.lang3.CharRange.<init>",
            "invalidMethodReference<init>"  // This won't match the pattern
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setClassName("org.apache.commons.lang3.CharRange");
        constructor.setMethodName("<init>");
        constructor.setParameters(List.of());
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "<init>", 269)
        ));

        when(repository.findByClassName("org.apache.commons.lang3.CharRange"))
            .thenReturn(List.of(constructor));

        Map<String, ConstructorContextEntity> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(1);
        verify(repository).findByClassName("org.apache.commons.lang3.CharRange");
    }
}


