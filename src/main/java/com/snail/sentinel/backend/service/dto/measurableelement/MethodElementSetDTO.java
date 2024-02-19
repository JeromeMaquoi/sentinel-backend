package com.snail.sentinel.backend.service.dto.measurableelement;

import java.util.ArrayList;
import java.util.List;

public class MethodElementSetDTO {
    private final List<MeasurableElementDTO> measurableElementDTOList;

    public MethodElementSetDTO() {
        this.measurableElementDTOList = new ArrayList<>();
    }

    public void add(MeasurableElementDTO measurableElementDTO) {
        this.measurableElementDTOList.add(measurableElementDTO);
    }

    public List<MeasurableElementDTO> getAll() {
        return this.measurableElementDTOList;
    }

    public boolean has(MeasurableElementDTO measurableElementDTO) {
        for (MeasurableElementDTO item : this.measurableElementDTOList) {
            if (item.equals(measurableElementDTO)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "MethodElementSetDTO{" +
            "MeasurableElementDTOList=" + measurableElementDTOList +
            '}';
    }
}
