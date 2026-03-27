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

    @Test
    void findMatchingConstructorsWithMultipleInitReturnsCorrectPositions() {
        // Test case from issue: callstack with multiple <init> calls should match constructors
        // at the correct positions, not at false-positive positions due to repeated method names
        List<String> callstack = List.of(
            "org.apache.commons.lang3.text.ExtendedMessageFormat.applyPattern",
            "org.apache.commons.lang3.text.ExtendedMessageFormat.<init>",
            "org.apache.commons.lang3.text.ExtendedMessageFormat.<init>",
            "org.apache.commons.lang3.text.ExtendedMessageFormatTest.checkBuiltInFormat",
            "org.apache.commons.lang3.text.ExtendedMessageFormatTest.checkBuiltInFormat",
            "org.apache.commons.lang3.text.ExtendedMessageFormatTest.checkBuiltInFormat",
            "org.apache.commons.lang3.text.ExtendedMessageFormatTest.testBuiltInChoiceFormat"
        );

        // Constructor 1: starts with <init> at line 118, then <init> at line 97
        ConstructorContextEntity constructor1 = new ConstructorContextEntity();
        constructor1.setId("constructor1");
        constructor1.setClassName("org.apache.commons.lang3.text.ExtendedMessageFormat");
        constructor1.setMethodName("<init>");
        constructor1.setStacktrace(List.of(
            new StackTraceElementDTO("ExtendedMessageFormat.java", "org.apache.commons.lang3.text.ExtendedMessageFormat", "<init>", 118),
            new StackTraceElementDTO("ExtendedMessageFormat.java", "org.apache.commons.lang3.text.ExtendedMessageFormat", "<init>", 97),
            new StackTraceElementDTO("ExtendedMessageFormatTest.java", "org.apache.commons.lang3.text.ExtendedMessageFormatTest", "checkBuiltInFormat", 205),
            new StackTraceElementDTO("ExtendedMessageFormatTest.java", "org.apache.commons.lang3.text.ExtendedMessageFormatTest", "checkBuiltInFormat", 221),
            new StackTraceElementDTO("ExtendedMessageFormatTest.java", "org.apache.commons.lang3.text.ExtendedMessageFormatTest", "checkBuiltInFormat", 234),
            new StackTraceElementDTO("ExtendedMessageFormatTest.java", "org.apache.commons.lang3.text.ExtendedMessageFormatTest", "testBuiltInChoiceFormat", 269)
        ));

        // Constructor 2: starts with <init> at line 100
        ConstructorContextEntity constructor2 = new ConstructorContextEntity();
        constructor2.setId("constructor2");
        constructor2.setClassName("org.apache.commons.lang3.text.ExtendedMessageFormat");
        constructor2.setMethodName("<init>");
        constructor2.setStacktrace(List.of(
            new StackTraceElementDTO("ExtendedMessageFormat.java", "org.apache.commons.lang3.text.ExtendedMessageFormat", "<init>", 100),
            new StackTraceElementDTO("ExtendedMessageFormatTest.java", "org.apache.commons.lang3.text.ExtendedMessageFormatTest", "checkBuiltInFormat", 205),
            new StackTraceElementDTO("ExtendedMessageFormatTest.java", "org.apache.commons.lang3.text.ExtendedMessageFormatTest", "checkBuiltInFormat", 221),
            new StackTraceElementDTO("ExtendedMessageFormatTest.java", "org.apache.commons.lang3.text.ExtendedMessageFormatTest", "checkBuiltInFormat", 234),
            new StackTraceElementDTO("ExtendedMessageFormatTest.java", "org.apache.commons.lang3.text.ExtendedMessageFormatTest", "testBuiltInChoiceFormat", 269)
        ));

        when(repository.findByClassName("org.apache.commons.lang3.text.ExtendedMessageFormat"))
            .thenReturn(List.of(constructor1, constructor2));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        // With stricter contiguous matching (no gaps allowed after first match):
        // - constructor1 should match at position 1 (stacktrace: <init>, <init>, checkBuiltInFormat, ... matches callstack[1,2,3,...])
        // - constructor2 should match at position 2 (stacktrace: <init>, checkBuiltInFormat, ... matches callstack[2,3,...])
        //   Constructor2 does NOT match at position 1 because after <init> at position 1,
        //   the next position is <init> (not checkBuiltInFormat), which creates a gap we can't allow
        // Total: 2 matches
        assertThat(result).hasSize(2);

        // Verify constructor1 is at position 1
        assertThat(result).anyMatch(m -> m.getConstructor().getId().equals("constructor1") && m.getCallstackPosition() == 1);

        // Verify constructor2 is at position 2
        assertThat(result).anyMatch(m -> m.getConstructor().getId().equals("constructor2") && m.getCallstackPosition() == 2);
    }
}
