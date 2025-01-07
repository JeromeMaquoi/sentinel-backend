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
        attributeEntity2.setName(attributeEntity1.getName());
        attributeEntity2.setType(attributeEntity1.getType());
        attributeEntity2.setActualType(attributeEntity1.getActualType());
        assertThat(attributeEntity1).isEqualTo(attributeEntity2);

        attributeEntity2 = getAttributeEntitySample2();
        assertThat(attributeEntity1).isNotEqualTo(attributeEntity2);
    }
}
