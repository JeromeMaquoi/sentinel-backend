package com.snail.sentinel.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommitEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommitEntity.class);
        CommitEntity commitEntity1 = new CommitEntity();
        commitEntity1.setId("id1");
        CommitEntity commitEntity2 = new CommitEntity();
        commitEntity2.setId(commitEntity1.getId());
        assertThat(commitEntity1).isEqualTo(commitEntity2);
        commitEntity2.setId("id2");
        assertThat(commitEntity1).isNotEqualTo(commitEntity2);
        commitEntity1.setId(null);
        assertThat(commitEntity1).isNotEqualTo(commitEntity2);
    }
}
