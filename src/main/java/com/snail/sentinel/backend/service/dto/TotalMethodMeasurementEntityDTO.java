package com.snail.sentinel.backend.service.dto;

public class TotalMethodMeasurementEntityDTO extends TotalMeasurementEntityDTO {
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
