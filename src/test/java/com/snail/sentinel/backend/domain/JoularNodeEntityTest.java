package com.snail.sentinel.backend.domain;

import static com.snail.sentinel.backend.domain.JoularNodeEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JoularNodeEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JoularNodeEntity.class);
        JoularNodeEntity joularNodeEntity1 = getJoularNodeEntitySample1();
        JoularNodeEntity joularNodeEntity2 = new JoularNodeEntity();
        assertThat(joularNodeEntity1).isNotEqualTo(joularNodeEntity2);

        joularNodeEntity2.setId(joularNodeEntity1.getId());
        joularNodeEntity2.setLineNumber(joularNodeEntity1.getLineNumber());
        assertThat(joularNodeEntity1).isEqualTo(joularNodeEntity2);

        joularNodeEntity2 = getJoularNodeEntitySample2();
        assertThat(joularNodeEntity1).isNotEqualTo(joularNodeEntity2);
    }
}
