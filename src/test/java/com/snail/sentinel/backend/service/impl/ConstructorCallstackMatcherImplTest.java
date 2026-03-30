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
    void findMatchingConstructorsWithNullCallstackTest() {
        AggregatedRuntimeCallTreeMeasurementDTO measurement = new AggregatedRuntimeCallTreeMeasurementDTO();
        measurement.setCallstack(null);
        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(measurement);
        assertThat(result).isEmpty();
        verify(repository, never()).findByClassName(anyString());
    }
    @Test
    void findMatchingConstructorsWithEmptyCallstackTest() {
        List<String> emptyCallstack = List.of();
        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(emptyCallstack);
        assertThat(result).isEmpty();
        verify(repository, never()).findByClassName(anyString());
    }
    @Test
    void findMatchingConstructorsWithSuccessfulMatchTest() {
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
    void findMatchingConstructorsWithMultipleInitReturnsCorrectPositionsTest() {
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

    @Test
    void findMatchingConstructorsWithCommitShaFilterTest() {
        String commitSha = "abc123def456";
        List<String> callstack = List.of(
            "org.example.MyClass.<init>",
            "org.example.MyClass.method"
        );
        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("123");
        constructor.setClassName("org.example.MyClass");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "<init>", 10),
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "method", 20)
        ));

        when(repository.findByClassNameAndCommitSha("org.example.MyClass", commitSha))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack, commitSha);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCallstackPosition()).isZero();
        verify(repository).findByClassNameAndCommitSha("org.example.MyClass", commitSha);
    }

    @Test
    void findMatchingConstructorsWithRepositoryFilterTest() {
        String repositoryName = "commons-lang";
        List<String> callstack = List.of(
            "org.apache.commons.lang3.CharRange.<init>"
        );
        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("456");
        constructor.setClassName("org.apache.commons.lang3.CharRange");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("CharRange.java", "org.apache.commons.lang3.CharRange", "<init>", 50)
        ));

        when(repository.findByClassNameAndCommitRepositoryName("org.apache.commons.lang3.CharRange", repositoryName))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructorsByRepository(callstack, repositoryName);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCallstackPosition()).isZero();
        verify(repository).findByClassNameAndCommitRepositoryName("org.apache.commons.lang3.CharRange", repositoryName);
    }

    @Test
    void findMatchingConstructorsWithNullStacktraceTest() {
        List<String> callstack = List.of(
            "org.example.MyClass.<init>",
            "org.example.MyClass.method"
        );
        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("789");
        constructor.setClassName("org.example.MyClass");
        constructor.setStacktrace(null);

        when(repository.findByClassName("org.example.MyClass"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).isEmpty();
    }

    @Test
    void findMatchingConstructorsWithEmptyStacktraceTest() {
        List<String> callstack = List.of(
            "org.example.MyClass.<init>"
        );
        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("789");
        constructor.setClassName("org.example.MyClass");
        constructor.setStacktrace(List.of());

        when(repository.findByClassName("org.example.MyClass"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).isEmpty();
    }

    @Test
    void findMatchingConstructorsWithMultipleClassesTest() {
        List<String> callstack = List.of(
            "org.example.ClassA.<init>",
            "org.example.ClassB.<init>",
            "org.example.ClassC.method"
        );
        ConstructorContextEntity constructorA = new ConstructorContextEntity();
        constructorA.setId("1");
        constructorA.setClassName("org.example.ClassA");
        constructorA.setStacktrace(List.of(
            new StackTraceElementDTO("ClassA.java", "org.example.ClassA", "<init>", 10),
            new StackTraceElementDTO("ClassB.java", "org.example.ClassB", "<init>", 20)
        ));

        ConstructorContextEntity constructorB = new ConstructorContextEntity();
        constructorB.setId("2");
        constructorB.setClassName("org.example.ClassB");
        constructorB.setStacktrace(List.of(
            new StackTraceElementDTO("ClassB.java", "org.example.ClassB", "<init>", 20),
            new StackTraceElementDTO("ClassC.java", "org.example.ClassC", "method", 30)
        ));

        when(repository.findByClassName("org.example.ClassA"))
            .thenReturn(List.of(constructorA));
        when(repository.findByClassName("org.example.ClassB"))
            .thenReturn(List.of(constructorB));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(2);
        assertThat(result).anyMatch(m -> m.getConstructor().getId().equals("1") && m.getCallstackPosition() == 0);
        assertThat(result).anyMatch(m -> m.getConstructor().getId().equals("2") && m.getCallstackPosition() == 1);
    }

    @Test
    void findMatchingConstructorsWithNoMatchingStacktracesTest() {
        List<String> callstack = List.of(
            "org.example.MyClass.<init>",
            "org.example.MyClass.method"
        );
        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("123");
        constructor.setClassName("org.example.MyClass");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "<init>", 10),
            new StackTraceElementDTO("OtherClass.java", "org.example.OtherClass", "otherMethod", 50)
        ));

        when(repository.findByClassName("org.example.MyClass"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).isEmpty();
    }

    @Test
    void findMatchingConstructorsWithAggregatedMeasurementAndCommitTest() {
        AggregatedRuntimeCallTreeMeasurementDTO measurement = new AggregatedRuntimeCallTreeMeasurementDTO();
        measurement.setCallstack(List.of(
            "org.example.MyClass.<init>",
            "org.example.MyClass.method"
        ));

        com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO commit = new com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO();
        commit.setSha("commitSha123");
        measurement.setCommit(commit);

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("999");
        constructor.setClassName("org.example.MyClass");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "<init>", 10),
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "method", 20)
        ));

        when(repository.findByClassNameAndCommitSha("org.example.MyClass", "commitSha123"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(measurement);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCallstackPosition()).isZero();
        verify(repository).findByClassNameAndCommitSha("org.example.MyClass", "commitSha123");
    }

    @Test
    void findMatchingConstructorsWithAggregatedMeasurementNullCommitTest() {
        AggregatedRuntimeCallTreeMeasurementDTO measurement = new AggregatedRuntimeCallTreeMeasurementDTO();
        measurement.setCallstack(List.of(
            "org.example.MyClass.<init>"
        ));
        measurement.setCommit(null);

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("888");
        constructor.setClassName("org.example.MyClass");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "<init>", 10)
        ));

        when(repository.findByClassName("org.example.MyClass"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(measurement);

        assertThat(result).hasSize(1);
        verify(repository).findByClassName("org.example.MyClass");
    }

    @Test
    void findMatchingConstructorsWithPartialStacktraceMatchTest() {
        List<String> callstack = List.of(
            "org.example.ClassA.<init>",
            "org.example.ClassB.<init>",
            "org.example.ClassB.method1",
            "org.example.ClassB.method2"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("partial");
        constructor.setClassName("org.example.ClassB");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("ClassB.java", "org.example.ClassB", "<init>", 20),
            new StackTraceElementDTO("ClassB.java", "org.example.ClassB", "method1", 30),
            new StackTraceElementDTO("ClassB.java", "org.example.ClassB", "method2", 40)
        ));

        when(repository.findByClassName("org.example.ClassB"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCallstackPosition()).isEqualTo(1);
    }

    @Test
    void findMatchingConstructorsWithSameConstructorAtMultiplePositionsTest() {
        List<String> callstack = List.of(
            "org.example.MyClass.<init>",
            "org.example.MyClass.method",
            "org.example.MyClass.<init>",
            "org.example.MyClass.method"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("repeat");
        constructor.setClassName("org.example.MyClass");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "<init>", 10),
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "method", 20)
        ));

        when(repository.findByClassName("org.example.MyClass"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCallstackPosition()).isZero();
        assertThat(result.get(1).getCallstackPosition()).isEqualTo(2);
    }

    @Test
    void findMatchingConstructorsWithCallstackNoInitMethodsTest() {
        List<String> callstack = List.of(
            "org.example.MyClass.method1",
            "org.example.MyClass.method2",
            "org.example.MyClass.method3"
        );

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).isEmpty();
        verify(repository, never()).findByClassName(anyString());
    }

    @Test
    void findMatchingConstructorsWithLongStacktraceTest() {
        List<String> callstack = List.of(
            "org.example.A.<init>",
            "org.example.B.method",
            "org.example.C.method",
            "org.example.D.method",
            "org.example.E.method",
            "org.example.F.method"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("long");
        constructor.setClassName("org.example.A");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("A.java", "org.example.A", "<init>", 1),
            new StackTraceElementDTO("B.java", "org.example.B", "method", 2),
            new StackTraceElementDTO("C.java", "org.example.C", "method", 3),
            new StackTraceElementDTO("D.java", "org.example.D", "method", 4),
            new StackTraceElementDTO("E.java", "org.example.E", "method", 5),
            new StackTraceElementDTO("F.java", "org.example.F", "method", 6)
        ));

        when(repository.findByClassName("org.example.A"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCallstackPosition()).isZero();
    }

    @Test
    void findMatchingConstructorsWithShortStacktraceMatchTest() {
        List<String> callstack = List.of(
            "org.example.MyClass.<init>",
            "org.example.OtherClass.method"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("short");
        constructor.setClassName("org.example.MyClass");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "<init>", 10)
        ));

        when(repository.findByClassName("org.example.MyClass"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCallstackPosition()).isZero();
    }

    @Test
    void findMatchingConstructorsWithConstructorStacktraceShortThanCallstackTest() {
        List<String> callstack = List.of(
            "org.example.A.<init>",
            "org.example.B.method",
            "org.example.C.method",
            "org.example.D.method"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("short_stack");
        constructor.setClassName("org.example.A");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("A.java", "org.example.A", "<init>", 1),
            new StackTraceElementDTO("B.java", "org.example.B", "method", 2)
        ));

        when(repository.findByClassName("org.example.A"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCallstackPosition()).isZero();
    }

    @Test
    void findMatchingConstructorsWithMultipleConstructorsForSameClassTest() {
        List<String> callstack = List.of(
            "org.example.MyClass.<init>",
            "org.example.MyClass.method1",
            "org.example.MyClass.method2"
        );

        ConstructorContextEntity constructor1 = new ConstructorContextEntity();
        constructor1.setId("ctor1");
        constructor1.setClassName("org.example.MyClass");
        constructor1.setStacktrace(List.of(
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "<init>", 10),
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "method1", 20)
        ));

        ConstructorContextEntity constructor2 = new ConstructorContextEntity();
        constructor2.setId("ctor2");
        constructor2.setClassName("org.example.MyClass");
        constructor2.setStacktrace(List.of(
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "<init>", 10),
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "method1", 20),
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "method2", 30)
        ));

        when(repository.findByClassName("org.example.MyClass"))
            .thenReturn(List.of(constructor1, constructor2));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).hasSize(2);
        assertThat(result).anyMatch(m -> m.getConstructor().getId().equals("ctor1") && m.getCallstackPosition() == 0);
        assertThat(result).anyMatch(m -> m.getConstructor().getId().equals("ctor2") && m.getCallstackPosition() == 0);
    }

    @Test
    void findMatchingConstructorsWithPartialMethodNameMatchTest() {
        List<String> callstack = List.of(
            "org.example.MyClass.<init>",
            "org.example.MyClass.methodA",
            "org.example.MyClass.methodB"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("partial");
        constructor.setClassName("org.example.MyClass");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "<init>", 10),
            new StackTraceElementDTO("MyClass.java", "org.example.MyClass", "methodX", 20)
        ));

        when(repository.findByClassName("org.example.MyClass"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).isEmpty();
    }

    @Test
    void findMatchingConstructorsWithGapsInStacktraceTest() {
        List<String> callstack = List.of(
            "org.example.A.<init>",
            "org.example.B.method1",
            "org.example.X.unknown",
            "org.example.B.method2"
        );

        ConstructorContextEntity constructor = new ConstructorContextEntity();
        constructor.setId("gap");
        constructor.setClassName("org.example.A");
        constructor.setStacktrace(List.of(
            new StackTraceElementDTO("A.java", "org.example.A", "<init>", 1),
            new StackTraceElementDTO("B.java", "org.example.B", "method1", 2),
            new StackTraceElementDTO("B.java", "org.example.B", "method2", 3)
        ));

        when(repository.findByClassName("org.example.A"))
            .thenReturn(List.of(constructor));

        List<MatchedConstructorDTO> result = matcher.findMatchingConstructors(callstack);

        assertThat(result).isEmpty();
    }
}
