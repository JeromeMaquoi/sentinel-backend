package com.snail.sentinel.backend.domain;

import java.util.UUID;

public class ConstructorEntityTestSamples {

    public static ConstructorEntity getConstructorEntitySample1() {
        return new ConstructorEntity().id("id1").name("name1").signature("signature1").className("className1").file("file1");
    }

    public static ConstructorEntity getConstructorEntitySample2() {
        return new ConstructorEntity().id("id2").name("name2").signature("signature2").className("className2").file("file2");
    }

    public static ConstructorEntity getConstructorEntityRandomSampleGenerator() {
        return new ConstructorEntity()
            .id(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .signature(UUID.randomUUID().toString())
            .className(UUID.randomUUID().toString())
            .file(UUID.randomUUID().toString());
    }
}
