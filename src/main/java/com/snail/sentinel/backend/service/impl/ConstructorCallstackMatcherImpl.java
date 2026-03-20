package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.repository.ConstructorContextEntityRepository;
import com.snail.sentinel.backend.service.ConstructorCallstackMatcher;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConstructorCallstackMatcherImpl implements ConstructorCallstackMatcher {
    private static final Logger log = LoggerFactory.getLogger(ConstructorCallstackMatcherImpl.class);

    // Pattern to extract class name from a method reference like "org.apache.commons.lang3.CharRange.<init>"
    private static final Pattern CLASS_METHOD_PATTERN = Pattern.compile("^(.+)\\.<init>$");

    private final ConstructorContextEntityRepository constructorContextEntityRepository;

    public ConstructorCallstackMatcherImpl(ConstructorContextEntityRepository constructorContextEntityRepository) {
        this.constructorContextEntityRepository = constructorContextEntityRepository;
    }

    @Override
    public Map<String, ConstructorContextEntity> findMatchingConstructors(AggregatedRuntimeCallTreeMeasurementDTO aggregatedMeasurement) {
        if (aggregatedMeasurement == null || aggregatedMeasurement.getCallstack() == null) {
            return Collections.emptyMap();
        }
        return findMatchingConstructors(aggregatedMeasurement.getCallstack());
    }

    @Override
    public Map<String, ConstructorContextEntity> findMatchingConstructors(List<String> callstack) {
        if (callstack == null || callstack.isEmpty()) {
            return Collections.emptyMap();
        }

        log.debug("Finding matching constructors for callstack of size: {}", callstack.size());

        Map<String, ConstructorContextEntity> matches = new LinkedHashMap<>();

        // Extract all constructor calls from the callstack
        Map<Integer, String> constructorCalls = extractConstructorCalls(callstack);

        if (constructorCalls.isEmpty()) {
            log.debug("No constructor calls found in callstack");
            return Collections.emptyMap();
        }

        // For each constructor call found in the callstack
        for (String constructorCallStr : constructorCalls.values()) {
            String className = extractClassName(constructorCallStr);

            if (className == null) {
                log.debug("Could not extract class name from constructor call: {}", constructorCallStr);
                continue;
            }

            // Find all ConstructorContextEntity documents with this class name
            List<ConstructorContextEntity> candidateConstructors = constructorContextEntityRepository.findByClassName(className);

            log.debug("Found {} candidate constructors for class: {}", candidateConstructors.size(), className);

            // Check each candidate to see if its stacktrace matches
            for (ConstructorContextEntity candidate : candidateConstructors) {
                if (isStacktraceSubsequence(callstack, candidate)) {
                    String key = generateKey(candidate);
                    matches.put(key, candidate);
                    log.debug("Matched constructor: {}", key);
                }
            }
        }

        return matches;
    }

    @Override
    public boolean isStacktraceSubsequence(List<String> runtimeCallstack, ConstructorContextEntity constructor) {
        if (constructor == null || constructor.getStacktrace() == null || constructor.getStacktrace().isEmpty()) {
            return false;
        }

        // Convert constructor's stacktrace to a list of method references
        List<String> constructorStacktraceSignatures = constructor.getStacktrace().stream()
            .map(this::stackTraceElementToMethodReference)
            .toList();

        // Check if constructor's stacktrace appears as a subsequence in the runtime callstack
        return isSubsequence(runtimeCallstack, constructorStacktraceSignatures);
    }

    /**
     * Checks if subsequence appears in sequence (allowing gaps).
     * This implements a simple subsequence matching algorithm.
     *
     * @param sequence The full sequence to search in
     * @param subsequence The subsequence to find
     * @return true if subsequence is found as a subsequence of sequence
     */
    private boolean isSubsequence(List<String> sequence, List<String> subsequence) {
        if (subsequence.isEmpty()) {
            return true;
        }
        if (sequence.isEmpty()) {
            return false;
        }

        int seqIdx = 0;
        int subIdx = 0;

        while (seqIdx < sequence.size() && subIdx < subsequence.size()) {
            // Normalize both strings for comparison (extract method name if it's a full reference)
            String seqMethodName = extractMethodNameFromReference(sequence.get(seqIdx));
            String subMethodName = extractMethodNameFromReference(subsequence.get(subIdx));

            if (seqMethodName.equals(subMethodName)) {
                subIdx++;
            }
            seqIdx++;
        }

        return subIdx == subsequence.size();
    }

    /**
     * Extracts method name from a full method reference.
     * E.g., "org.apache.commons.lang3.CharRange.is" -> "is"
     *       "org.apache.commons.lang3.CharRange.<init>" -> "<init>"
     */
    private String extractMethodNameFromReference(String methodReference) {
        if (methodReference == null) {
            return "";
        }
        int lastDotIndex = methodReference.lastIndexOf('.');
        if (lastDotIndex >= 0 && lastDotIndex < methodReference.length() - 1) {
            return methodReference.substring(lastDotIndex + 1);
        }
        return methodReference;
    }

    /**
     * Converts a StackTraceElementDTO to a full method reference.
     * E.g., className="org.apache.commons.lang3.CharRange", methodName="<init>"
     *       -> "org.apache.commons.lang3.CharRange.<init>"
     */
    private String stackTraceElementToMethodReference(StackTraceElementDTO element) {
        return element.getClassName() + "." + element.getMethodName();
    }

    /**
     * Extracts all constructor calls from a callstack.
     * Returns a map of index -> full method reference for constructor calls.
     */
    private Map<Integer, String> extractConstructorCalls(List<String> callstack) {
        Map<Integer, String> constructorCalls = new LinkedHashMap<>();

        for (int i = 0; i < callstack.size(); i++) {
            String methodRef = callstack.get(i);
            if (methodRef != null && methodRef.contains("<init>")) {
                constructorCalls.put(i, methodRef);
            }
        }

        return constructorCalls;
    }

    /**
     * Extracts the class name from a constructor call reference.
     * E.g., "org.apache.commons.lang3.CharRange.<init>" -> "org.apache.commons.lang3.CharRange"
     */
    private String extractClassName(String constructorCall) {
        Matcher matcher = CLASS_METHOD_PATTERN.matcher(constructorCall);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Generates a unique key for a constructor entity.
     */
    private String generateKey(ConstructorContextEntity constructor) {
        return String.format("%s#%s(%s)",
            constructor.getClassName(),
            constructor.getMethodName(),
            String.join(",", constructor.getParameters() != null ? constructor.getParameters() : List.of())
        );
    }
}



