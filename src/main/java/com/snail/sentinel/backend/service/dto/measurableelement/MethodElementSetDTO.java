package com.snail.sentinel.backend.service.dto.measurableelement;

import java.util.ArrayList;
import java.util.List;

public class MethodElementSetDTO {
    private final List<MeasurableElementDTO> MeasurableElementDTOList;

    public MethodElementSetDTO() {
        this.MeasurableElementDTOList = new ArrayList<>();
    }

    public void add(MeasurableElementDTO MeasurableElementDTO) {
        this.MeasurableElementDTOList.add(MeasurableElementDTO);
    }

    public List<MeasurableElementDTO> getAll() {
        return this.MeasurableElementDTOList;
    }

    public boolean has(MeasurableElementDTO MeasurableElementDTO) {
        for (MeasurableElementDTO item : this.MeasurableElementDTOList) {
            if (item.equals(MeasurableElementDTO)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "MethodElementSetDTO{" +
            "MeasurableElementDTOList=" + MeasurableElementDTOList +
            '}';
    }
}
