package com.snail.sentinel.backend.domain;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "joularjx_measurements")
@TypeAlias("total_method")
public class TotalMethodMeasurementEntity extends TotalMeasurementEntity {
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
