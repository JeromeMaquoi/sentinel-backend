package com.snail.sentinel.backend.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class StackTraceElementJoularEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StackTraceElementJoularEntity getStackTraceElementJoularEntitySample1() {
        return new StackTraceElementJoularEntity()
            .id("id1")
            .lineNumber(1)
            .constructorElement("constructorElement1")
            .parent("parent1")
            .ancestors("ancestors1")
            .consumptionValues("consumptionValues1")
            .commit("commit1");
    }

    public static StackTraceElementJoularEntity getStackTraceElementJoularEntitySample2() {
        return new StackTraceElementJoularEntity()
            .id("id2")
            .lineNumber(2)
            .constructorElement("constructorElement2")
            .parent("parent2")
            .ancestors("ancestors2")
            .consumptionValues("consumptionValues2")
            .commit("commit2");
    }

    public static StackTraceElementJoularEntity getStackTraceElementJoularEntityRandomSampleGenerator() {
        return new StackTraceElementJoularEntity()
            .id(UUID.randomUUID().toString())
            .lineNumber(intCount.incrementAndGet())
            .constructorElement(UUID.randomUUID().toString())
            .parent(UUID.randomUUID().toString())
            .ancestors(UUID.randomUUID().toString())
            .consumptionValues(UUID.randomUUID().toString())
            .commit(UUID.randomUUID().toString());
    }
}
