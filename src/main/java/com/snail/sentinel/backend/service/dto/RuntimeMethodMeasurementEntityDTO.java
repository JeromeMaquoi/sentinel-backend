package com.snail.sentinel.backend.service.dto;

public class RuntimeMethodMeasurementEntityDTO extends RuntimeMeasurementEntityDTO {
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
