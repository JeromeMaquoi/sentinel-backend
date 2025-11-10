package com.snail.sentinel.backend.domain;

public abstract class RuntimeMeasurementEntity extends AbstractEnergyMeasurementEntity {
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
