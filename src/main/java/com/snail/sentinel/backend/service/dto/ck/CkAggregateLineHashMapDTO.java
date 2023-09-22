package com.snail.sentinel.backend.service.dto.ck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CkAggregateLineHashMapDTO {

    private final Logger log = LoggerFactory.getLogger(CkAggregateLineHashMapDTO.class);
    Map<String, CkAggregateLineDTO> ckAggregateLineHashMapDTO = new HashMap<>();

    public Map<String, CkAggregateLineDTO> getAll() {
        return ckAggregateLineHashMapDTO;
    }

    public void setCkAggregateLineHashMapDTO(List<CkAggregateLineDTO> ckAggregateLineDTOList) {
        for (CkAggregateLineDTO item : ckAggregateLineDTOList) {
            this.insertOne(item);
        }
    }

    public List<CkAggregateLineDTO> getAllOccurrences(String className, String methodName) {
        List<CkAggregateLineDTO> listOccurrences = new ArrayList<>();
        for (Map.Entry<String, CkAggregateLineDTO> set: this.ckAggregateLineHashMapDTO.entrySet()) {
            if (set.getKey().contains(className) && set.getKey().contains(methodName)) {
                listOccurrences.add(set.getValue());
            }
        }
        //log.info("All occurrences of {} and {} returned", className, methodName);
        return listOccurrences;
    }

    public CkAggregateLineDTO getCkAggregateLineDTO(String label) {
        return this.ckAggregateLineHashMapDTO.get(label);
    }

    public void insertOne(CkAggregateLineDTO ckAggregateLineDTO) {
        String label = ckAggregateLineDTO.getLabel();
        this.ckAggregateLineHashMapDTO.put(label, ckAggregateLineDTO);
    }

    @Override
    public String toString() {
        return "CkAggregateLineListDTO{" +
            "ckAggregateLineDTOHashMap=" + ckAggregateLineHashMapDTO +
            '}';
    }
}
