package com.snail.sentinel.backend.service.dto.joularNode;

import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoularNodeHashMapDTO {
    Map<String, JoularNodeEntityDTO> joularNodeEntityDTOMap = new HashMap<>();

    public void setJoularNodeEntityDTOMap(List<JoularNodeEntityDTO> joularNodeEntityDTOList) {
        for (JoularNodeEntityDTO joularNodeEntityDTO : joularNodeEntityDTOList) {
            this.insertOne(joularNodeEntityDTO);
        }
    }

    public void insertOne(JoularNodeEntityDTO joularNodeEntityDTO) {
        String label = joularNodeEntityDTO.getLabel();
        this.joularNodeEntityDTOMap.put(label, joularNodeEntityDTO);
    }

    public boolean isJoularNodeEntityDTOInMap(String label) {
        return this.joularNodeEntityDTOMap.containsKey(label);
    }

    public JoularNodeEntityDTO getJoularNodeEntityDTO(String label) {
        return this.joularNodeEntityDTOMap.get(label);
    }
}
