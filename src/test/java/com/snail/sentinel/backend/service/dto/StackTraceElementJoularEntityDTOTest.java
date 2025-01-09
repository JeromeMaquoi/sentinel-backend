package com.snail.sentinel.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StackTraceElementJoularEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StackTraceElementJoularEntityDTO.class);
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO1 = new StackTraceElementJoularEntityDTO();
        stackTraceElementJoularEntityDTO1.setId("id1");
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO2 = new StackTraceElementJoularEntityDTO();
        assertThat(stackTraceElementJoularEntityDTO1).isNotEqualTo(stackTraceElementJoularEntityDTO2);
        stackTraceElementJoularEntityDTO2.setId(stackTraceElementJoularEntityDTO1.getId());
        assertThat(stackTraceElementJoularEntityDTO1).isEqualTo(stackTraceElementJoularEntityDTO2);
        stackTraceElementJoularEntityDTO2.setId("id2");
        assertThat(stackTraceElementJoularEntityDTO1).isNotEqualTo(stackTraceElementJoularEntityDTO2);
        stackTraceElementJoularEntityDTO1.setId(null);
        assertThat(stackTraceElementJoularEntityDTO1).isNotEqualTo(stackTraceElementJoularEntityDTO2);
    }
}
