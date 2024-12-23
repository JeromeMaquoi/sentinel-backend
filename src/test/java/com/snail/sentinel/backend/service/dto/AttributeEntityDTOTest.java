package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AttributeEntityDTOTest {
    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeEntityDTO.class);
        AttributeEntityDTO attributeEntityDTO1 = new AttributeEntityDTO();
        attributeEntityDTO1.setName("name");
        AttributeEntityDTO attributeEntityDTO2 = new AttributeEntityDTO();
        assertNotEquals(attributeEntityDTO1, attributeEntityDTO2);
        attributeEntityDTO2.setName(attributeEntityDTO1.getName());
        assertEquals(attributeEntityDTO1, attributeEntityDTO2);
        attributeEntityDTO2.setName("name2");
        assertNotEquals(attributeEntityDTO1, attributeEntityDTO2);
        attributeEntityDTO1.setName(null);
        assertNotEquals(attributeEntityDTO1, attributeEntityDTO2);
    }
}
