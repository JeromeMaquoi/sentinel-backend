package com.snail.sentinel.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CkEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CkEntityDTO.class);
        CkEntityDTO ckEntityDTO1 = new CkEntityDTO();
        ckEntityDTO1.setId("id1");
        CkEntityDTO ckEntityDTO2 = new CkEntityDTO();
        assertThat(ckEntityDTO1).isNotEqualTo(ckEntityDTO2);
        ckEntityDTO2.setId(ckEntityDTO1.getId());
        assertThat(ckEntityDTO1).isEqualTo(ckEntityDTO2);
        ckEntityDTO2.setId("id2");
        assertThat(ckEntityDTO1).isNotEqualTo(ckEntityDTO2);
        ckEntityDTO1.setId(null);
        assertThat(ckEntityDTO1).isNotEqualTo(ckEntityDTO2);
    }
}
