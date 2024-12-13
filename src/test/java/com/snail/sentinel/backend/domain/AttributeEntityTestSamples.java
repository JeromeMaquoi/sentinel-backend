package com.snail.sentinel.backend.domain;

import java.util.UUID;

public class AttributeEntityTestSamples {

    public static AttributeEntity getAttributeEntitySample1() {
        return new AttributeEntity().id("id1").name("name1").type("type1");
    }

    public static AttributeEntity getAttributeEntitySample2() {
        return new AttributeEntity().id("id2").name("name2").type("type2");
    }

    public static AttributeEntity getAttributeEntityRandomSampleGenerator() {
        return new AttributeEntity().id(UUID.randomUUID().toString()).name(UUID.randomUUID().toString()).type(UUID.randomUUID().toString());
    }
}
