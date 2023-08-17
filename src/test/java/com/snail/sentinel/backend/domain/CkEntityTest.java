package com.snail.sentinel.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CkEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CkEntity.class);
        CkEntity ckEntity1 = new CkEntity();
        ckEntity1.setId("id1");
        CkEntity ckEntity2 = new CkEntity();
        ckEntity2.setId(ckEntity1.getId());
        assertThat(ckEntity1).isEqualTo(ckEntity2);
        ckEntity2.setId("id2");
        assertThat(ckEntity1).isNotEqualTo(ckEntity2);
        ckEntity1.setId(null);
        assertThat(ckEntity1).isNotEqualTo(ckEntity2);
    }
}
