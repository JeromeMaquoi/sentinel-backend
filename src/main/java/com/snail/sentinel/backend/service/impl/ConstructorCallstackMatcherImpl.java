package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.repository.ConstructorContextEntityRepository;
import com.snail.sentinel.backend.service.ConstructorCallstackMatcher;
import com.snail.sentinel.backend.service.dto.MatchedConstructorDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConstructorCallstackMatcherImpl implements ConstructorCallstackMatcher {
    private static final Logger log = LoggerFactory.getLogger(ConstructorCallstackMatcherImpl.class);

    private final ConstructorContextEntityRepository constructorContextEntityRepository;

    public ConstructorCallstackMatcherImpl(ConstructorContextEntityRepository constructorContextEntityRepository) {
        this.constructorContextEntityRepository = constructorContextEntityRepository;
    }

    @Override
    public List<MatchedConstructorDTO> findMatchingConstructors(AggregatedRuntimeCallTreeMeasurementDTO aggregatedMeasurement) {
        if (aggregatedMeasurement == null || aggregatedMeasurement.getCallstack() == null) {
            return Collections.emptyList();
        }
        return findMatchingConstructors(aggregatedMeasurement.getCallstack());
    }

    @Override
    public List<MatchedConstructorDTO> findMatchingConstructors(List<String> callstack) {
        if (callstack == null || callstack.isEmpty()) {
            return Collections.emptyList();
        }

        log.debug("Finding matching constructors for callstack of size: {}", callstack.size());

        // Use a LinkedHashMap to maintain insertion order and ensure uniqueness by position + constructor ID
        Map<String, MatchedConstructorDTO> uniqueMatches = new LinkedHashMap<>();

        // Extract all constructor calls with their positions and class names
        Map<Integer, String> constructorCalls = extractConstructorCalls(callstack);

        if (constructorCalls.isEmpty()) {
            log.debug("No constructor calls found in callstack");
            return Collections.emptyList();
        }

        // Get unique class names from constructor calls to minimize database queries
        Set<String> classNamesToSearch = new HashSet<>();
        for (String constructorCall : constructorCalls.values()) {
            String className = extractClassName(constructorCall);
            if (className != null) {
                classNamesToSearch.add(className);
            }
        }

        log.debug("Found {} unique classes in callstack constructor calls", classNamesToSearch.size());

        // For each class, query only the constructors for that class
        for (String className : classNamesToSearch) {
            List<ConstructorContextEntity> constructorsForClass = constructorContextEntityRepository.findByClassName(className);

            log.debug("Found {} candidate constructors for class: {}", constructorsForClass.size(), className);

            // Check each constructor to find which positions it matches
            for (ConstructorContextEntity candidate : constructorsForClass) {
                if (candidate.getStacktrace() == null || candidate.getStacktrace().isEmpty()) {
                    continue;
                }

                // Find all positions in the callstack where this constructor's stacktrace matches
                List<Integer> matchingPositions = findMatchingPositions(callstack, candidate);

                for (Integer position : matchingPositions) {
                    String constructorId = candidate.getId();

                    // Use composite key of position + constructorId to allow same constructor at different positions
                    String uniqueKey = position + "_" + constructorId;
                    uniqueMatches.computeIfAbsent(uniqueKey, k -> {
                        MatchedConstructorDTO matchedConstructor = new MatchedConstructorDTO();
                        matchedConstructor.setCallstackPosition(position);
                        matchedConstructor.setConstructor(candidate);
                        log.debug("Matched constructor: {} at position {}", candidate.getClassName(), position);
                        return matchedConstructor;
                    });
                }
            }
        }

        return new ArrayList<>(uniqueMatches.values());
    }

    /**
     * Finds all positions in the callstack where a constructor's stacktrace appears as a subsequence.
     * Returns the positions of the <init> calls that serve as the starting point for each match.
     */
    private List<Integer> findMatchingPositions(List<String> callstack, ConstructorContextEntity constructor) {
        List<Integer> matchingPositions = new ArrayList<>();

        // Convert constructor's stacktrace to method reference signatures
        List<String> constructorStacktraceSignatures = constructor.getStacktrace().stream()
            .map(this::stackTraceElementToMethodReference)
            .toList();

        if (constructorStacktraceSignatures.isEmpty()) {
            return matchingPositions;
        }

        // Get all <init> positions in the callstack
        Map<Integer, String> constructorCalls = extractConstructorCalls(callstack);

        // For each constructor position, check if the constructor's stacktrace appears starting from that position
        for (Integer initPosition : constructorCalls.keySet()) {
            // Try to match the constructor's stacktrace starting from this <init> position
            if (isSubsequenceStartingAt(callstack, initPosition, constructorStacktraceSignatures)) {
                matchingPositions.add(initPosition);
            }
        }

        return matchingPositions;
    }

    /**
     * Checks if the constructor's stacktrace appears as a subsequence starting from a specific position.
     */
    private boolean isSubsequenceStartingAt(List<String> callstack, Integer startPosition, List<String> constructorStacktrace) {
        if (startPosition >= callstack.size()) {
            return false;
        }

        int seqIdx = startPosition;
        int subIdx = 0;

        while (seqIdx < callstack.size() && subIdx < constructorStacktrace.size()) {
            String seqMethodName = extractMethodNameFromReference(callstack.get(seqIdx));
            String subMethodName = extractMethodNameFromReference(constructorStacktrace.get(subIdx));

            if (seqMethodName.equals(subMethodName)) {
                subIdx++;
            }
            seqIdx++;
        }

        return subIdx == constructorStacktrace.size();
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
        if (constructorCall == null) {
            return null;
        }
        int lastDotIndex = constructorCall.lastIndexOf(".<init>");
        if (lastDotIndex >= 0) {
            return constructorCall.substring(0, lastDotIndex);
        }
        return null;
    }
}



