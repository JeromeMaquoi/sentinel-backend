package com.snail.sentinel.backend.domain;

import static com.snail.sentinel.backend.domain.StackTraceElementJoularEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StackTraceElementJoularEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StackTraceElementJoularEntity.class);
        StackTraceElementJoularEntity stackTraceElementJoularEntity1 = getStackTraceElementJoularEntitySample1();
        StackTraceElementJoularEntity stackTraceElementJoularEntity2 = new StackTraceElementJoularEntity();
        assertThat(stackTraceElementJoularEntity1).isNotEqualTo(stackTraceElementJoularEntity2);

        stackTraceElementJoularEntity2.setId(stackTraceElementJoularEntity1.getId());
        assertThat(stackTraceElementJoularEntity1).isEqualTo(stackTraceElementJoularEntity2);

        stackTraceElementJoularEntity2 = getStackTraceElementJoularEntitySample2();
        assertThat(stackTraceElementJoularEntity1).isNotEqualTo(stackTraceElementJoularEntity2);
    }
}
