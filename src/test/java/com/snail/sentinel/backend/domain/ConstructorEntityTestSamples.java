package com.snail.sentinel.backend.domain;

import java.util.UUID;

public class ConstructorEntityTestSamples {

    public static ConstructorEntity getConstructorEntitySample1() {
        return new ConstructorEntity().id("id1").name("name1").signature("signature1").pkg("pkg1").file("file1");
    }

    public static ConstructorEntity getConstructorEntitySample2() {
        return new ConstructorEntity().id("id2").name("name2").signature("signature2").pkg("pkg2").file("file2");
    }

    public static ConstructorEntity getConstructorEntityRandomSampleGenerator() {
        return new ConstructorEntity()
            .id(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .signature(UUID.randomUUID().toString())
            .pkg(UUID.randomUUID().toString())
            .file(UUID.randomUUID().toString());
    }
}
