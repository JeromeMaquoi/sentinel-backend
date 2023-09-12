package com.snail.sentinel.backend.service.dto.measurableelement;

import java.util.ArrayList;
import java.util.List;

public class MethodElementSetDTO {
    private final List<MethodElementDTO> methodElementDTOList;

    public MethodElementSetDTO() {
        this.methodElementDTOList = new ArrayList<>();
    }

    public void add(MethodElementDTO methodElementDTO) {
        this.methodElementDTOList.add(methodElementDTO);
    }

    public List<MethodElementDTO> getAll() {
        return this.methodElementDTOList;
    }

    public boolean has(MethodElementDTO methodElementDTO) {
        for (MethodElementDTO item : this.methodElementDTOList) {
            if (item.equals(methodElementDTO)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "MethodElementSetDTO{" +
            "methodElementDTOList=" + methodElementDTOList +
            '}';
    }
}
