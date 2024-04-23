package com.snail.sentinel.backend.service.exceptions;

public class CreateJoularNodeEntityMeasurableElementException extends IllegalStateException {
    public CreateJoularNodeEntityMeasurableElementException(String classMethodLineString) {super("MeasurableElementDTO is not present for " + classMethodLineString);}
}
