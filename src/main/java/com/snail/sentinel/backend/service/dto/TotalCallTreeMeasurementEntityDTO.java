package com.snail.sentinel.backend.service.dto;

import java.io.Serializable;
import java.util.List;

public class TotalCallTreeMeasurementEntityDTO extends BaseMeasurementDTO {
    private List<String> callstack;
}
