package com.snail.sentinel.backend.domain;

import java.util.UUID;

public class CommitEntityTestSamples {

    public static CommitEntity getCommitEntitySample1() {
        return new CommitEntity().id("id1").sha("sha1");
    }

    public static CommitEntity getCommitEntitySample2() {
        return new CommitEntity().id("id2").sha("sha2");
    }

    public static CommitEntity getCommitEntityRandomSampleGenerator() {
        return new CommitEntity().id(UUID.randomUUID().toString()).sha(UUID.randomUUID().toString());
    }
}
