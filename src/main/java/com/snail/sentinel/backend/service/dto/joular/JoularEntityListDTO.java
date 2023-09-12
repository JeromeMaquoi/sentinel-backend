package com.snail.sentinel.backend.service.dto.joular;

import com.snail.sentinel.backend.service.dto.measurableelement.MethodElementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

public class JoularEntityListDTO {

    private final Logger log = LoggerFactory.getLogger(JoularEntityListDTO.class);

    List<JoularEntityDTO> joularEntityDTOList;

    public JoularEntityListDTO() {
        this.joularEntityDTOList = new ArrayList<>();
    }

    public void add(JoularEntityDTO joularEntityDTO) {
        this.joularEntityDTOList.add(joularEntityDTO);
    }

    public List<JoularEntityDTO> getList() {
        return this.joularEntityDTOList;
    }

    public void concat(JoularEntityListDTO firstJoularListDTO, JoularEntityListDTO secondEntityListDTO) {
        this.joularEntityDTOList = Stream.concat(firstJoularListDTO.getList().stream(), secondEntityListDTO.getList().stream()).toList();
    }

    public void update(MethodElementDTO methodElementDTO, Float newValue) {
        //log.info("Update asked for joularEntityDTOList");
        JoularEntityDTO dataToUpdate = getOneWithMethodElement(methodElementDTO);
        Float oldValue = dataToUpdate.getValue();
        dataToUpdate.setValue(oldValue + newValue);
    }

    private JoularEntityDTO getOneWithMethodElement(MethodElementDTO methodElementDTO) {
        for (JoularEntityDTO entity: this.joularEntityDTOList) {
            if (Boolean.TRUE.equals(entity.hasMethodElement(methodElementDTO))) {
                return entity;
            }
        }
        log.warn("Null returned for {} and {}", methodElementDTO, this.joularEntityDTOList);
        return null;
    }

    @Override
    public String toString() {
        return "JoularEntityListDTO{" +
            "joularEntityDTOList=" + joularEntityDTOList +
            '}';
    }
}
