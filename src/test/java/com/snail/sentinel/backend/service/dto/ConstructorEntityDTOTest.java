package com.snail.sentinel.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConstructorEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConstructorEntityDTO.class);
        ConstructorEntityDTO constructorEntityDTO1 = new ConstructorEntityDTO();
        constructorEntityDTO1.setId("id1");
        ConstructorEntityDTO constructorEntityDTO2 = new ConstructorEntityDTO();
        assertThat(constructorEntityDTO1).isNotEqualTo(constructorEntityDTO2);
        constructorEntityDTO2.setId(constructorEntityDTO1.getId());
        assertThat(constructorEntityDTO1).isEqualTo(constructorEntityDTO2);
        constructorEntityDTO2.setId("id2");
        assertThat(constructorEntityDTO1).isNotEqualTo(constructorEntityDTO2);
        constructorEntityDTO1.setId(null);
        assertThat(constructorEntityDTO1).isNotEqualTo(constructorEntityDTO2);
    }
}
