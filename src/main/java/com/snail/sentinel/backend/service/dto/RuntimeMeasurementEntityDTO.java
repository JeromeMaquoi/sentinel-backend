package com.snail.sentinel.backend.service.dto;

public class RuntimeMeasurementEntityDTO extends BaseMeasurementDTO {
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
