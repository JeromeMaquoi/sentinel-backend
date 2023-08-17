package com.snail.sentinel.backend.service.dto.measurableelement;

import jakarta.validation.constraints.NotNull;

public class MethodElementDTO extends MeasurableElementDTO {
    @NotNull
    private String methodSignature;

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }
}
