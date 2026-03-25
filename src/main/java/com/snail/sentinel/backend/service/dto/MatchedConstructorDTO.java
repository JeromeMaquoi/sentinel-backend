package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;

public class MatchedConstructorDTO {
    private int callstackPosition;
    private ConstructorContextEntity constructor;

    public int getCallstackPosition() {
        return callstackPosition;
    }

    public void setCallstackPosition(int callstackPosition) {
        this.callstackPosition = callstackPosition;
    }

    public ConstructorContextEntity getConstructor() {
        return constructor;
    }

    public void setConstructor(ConstructorContextEntity constructor) {
        this.constructor = constructor;
    }

    @Override
    public String toString() {
        return "MatchedConstructorDTO{" +
            "callstackPosition=" + callstackPosition +
            ", constructor=" + constructor +
            '}';
    }
}
