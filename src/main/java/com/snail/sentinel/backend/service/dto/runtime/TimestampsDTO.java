package com.snail.sentinel.backend.service.dto.runtime;

import java.util.List;

public class TimestampsDTO {
    private List<Long> timestamps;

    public List<Long> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
    }

    public TimestampsDTO withTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
        return this;
    }

    @Override
    public String toString() {
        return "RuntimeTimestampsDTO{" +
            "timestamps=" + timestamps +
            '}';
    }
}
