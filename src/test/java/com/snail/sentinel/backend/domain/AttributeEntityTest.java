package com.snail.sentinel.backend.domain;

import static com.snail.sentinel.backend.domain.AttributeEntityTestSamples.*;
import static com.snail.sentinel.backend.domain.ConstructorEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttributeEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeEntity.class);
        AttributeEntity attributeEntity1 = getAttributeEntitySample1();
        AttributeEntity attributeEntity2 = new AttributeEntity();
        assertThat(attributeEntity1).isNotEqualTo(attributeEntity2);

        attributeEntity2.setId(attributeEntity1.getId());
        assertThat(attributeEntity1).isEqualTo(attributeEntity2);

        attributeEntity2 = getAttributeEntitySample2();
        assertThat(attributeEntity1).isNotEqualTo(attributeEntity2);
    }

    @Test
    void constructorEntityTest() {
        AttributeEntity attributeEntity = getAttributeEntityRandomSampleGenerator();
        ConstructorEntity constructorEntityBack = getConstructorEntityRandomSampleGenerator();

        attributeEntity.setConstructorEntity(constructorEntityBack);
        assertThat(attributeEntity.getConstructorEntity()).isEqualTo(constructorEntityBack);

        attributeEntity.constructorEntity(null);
        assertThat(attributeEntity.getConstructorEntity()).isNull();
    }
}
