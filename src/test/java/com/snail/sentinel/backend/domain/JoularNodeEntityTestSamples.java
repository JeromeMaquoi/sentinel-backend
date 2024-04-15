package com.snail.sentinel.backend.domain;


public class JoularNodeEntityTestSamples {

    public static JoularNodeEntity getJoularNodeEntitySample1() {
        return new JoularNodeEntity().id("id1").lineNumber(1);
    }

    public static JoularNodeEntity getJoularNodeEntitySample2() {
        return new JoularNodeEntity().id("id2").lineNumber(2);
    }
}
