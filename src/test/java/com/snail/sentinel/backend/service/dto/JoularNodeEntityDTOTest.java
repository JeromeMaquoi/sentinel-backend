package com.snail.sentinel.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JoularNodeEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JoularNodeEntityDTO.class);
        JoularNodeEntityDTO joularNodeEntityDTO1 = new JoularNodeEntityDTO();
        joularNodeEntityDTO1.setId("id1");
        JoularNodeEntityDTO joularNodeEntityDTO2 = new JoularNodeEntityDTO();
        assertThat(joularNodeEntityDTO1).isNotEqualTo(joularNodeEntityDTO2);
        joularNodeEntityDTO2.setId(joularNodeEntityDTO1.getId());
        assertThat(joularNodeEntityDTO1).isEqualTo(joularNodeEntityDTO2);
        joularNodeEntityDTO2.setId("id2");
        assertThat(joularNodeEntityDTO1).isNotEqualTo(joularNodeEntityDTO2);
        joularNodeEntityDTO1.setId(null);
        assertThat(joularNodeEntityDTO1).isNotEqualTo(joularNodeEntityDTO2);
    }
}
