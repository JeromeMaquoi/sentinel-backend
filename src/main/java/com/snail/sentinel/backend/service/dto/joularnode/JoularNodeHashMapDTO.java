package com.snail.sentinel.backend.service.dto.joularnode;

import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;

import java.util.HashMap;
import java.util.Map;

public class JoularNodeHashMapDTO {
    Map<String, JoularNodeEntityDTO> joularNodeEntityDTOMap = new HashMap<>();

    public void insertOne(JoularNodeEntityDTO joularNodeEntityDTO) {
        String label = joularNodeEntityDTO.getLabel();
        joularNodeEntityDTOMap.put(label, joularNodeEntityDTO);
    }

    public boolean isJoularNodeEntityDTOInMap(String label) {
        return this.joularNodeEntityDTOMap.containsKey(label);
    }

    public JoularNodeEntityDTO getJoularNodeEntityDTO(String label) {
        return joularNodeEntityDTOMap.get(label);
    }
}
