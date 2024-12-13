package com.snail.sentinel.backend.domain;

import static com.snail.sentinel.backend.domain.AttributeEntityTestSamples.*;
import static com.snail.sentinel.backend.domain.ConstructorEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConstructorEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConstructorEntity.class);
        ConstructorEntity constructorEntity1 = getConstructorEntitySample1();
        ConstructorEntity constructorEntity2 = new ConstructorEntity();
        assertThat(constructorEntity1).isNotEqualTo(constructorEntity2);

        constructorEntity2.setId(constructorEntity1.getId());
        assertThat(constructorEntity1).isEqualTo(constructorEntity2);

        constructorEntity2 = getConstructorEntitySample2();
        assertThat(constructorEntity1).isNotEqualTo(constructorEntity2);
    }

    @Test
    void attributeEntityTest() {
        ConstructorEntity constructorEntity = getConstructorEntityRandomSampleGenerator();
        AttributeEntity attributeEntityBack = getAttributeEntityRandomSampleGenerator();

        constructorEntity.addAttributeEntity(attributeEntityBack);
        assertThat(constructorEntity.getAttributeEntities()).containsOnly(attributeEntityBack);
        assertThat(attributeEntityBack.getConstructorEntity()).isEqualTo(constructorEntity);

        constructorEntity.removeAttributeEntity(attributeEntityBack);
        assertThat(constructorEntity.getAttributeEntities()).doesNotContain(attributeEntityBack);
        assertThat(attributeEntityBack.getConstructorEntity()).isNull();

        constructorEntity.attributeEntities(new HashSet<>(Set.of(attributeEntityBack)));
        assertThat(constructorEntity.getAttributeEntities()).containsOnly(attributeEntityBack);
        assertThat(attributeEntityBack.getConstructorEntity()).isEqualTo(constructorEntity);

        constructorEntity.setAttributeEntities(new HashSet<>());
        assertThat(constructorEntity.getAttributeEntities()).doesNotContain(attributeEntityBack);
        assertThat(attributeEntityBack.getConstructorEntity()).isNull();
    }
}
