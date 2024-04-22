package com.snail.sentinel.backend.service.dto.joularnode;

import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JoularNodeHashMapDTO {
    Map<JoularNodeKeyHashMap, JoularNodeEntityDTO> joularNodeEntityDTOMap = new HashMap<>();

    public void insertOne(JoularNodeEntityDTO joularNodeEntityDTO) {
        String classMethodSignature = joularNodeEntityDTO.getMeasurableElement().getClassMethodSignature();
        int lineNumber = joularNodeEntityDTO.getLineNumber();
        List<String> ancestors = joularNodeEntityDTO.getAncestors();
        JoularNodeKeyHashMap key = new JoularNodeKeyHashMap(classMethodSignature, lineNumber, ancestors);
        joularNodeEntityDTOMap.put(key, joularNodeEntityDTO);
    }

    public boolean isJoularNodeEntityDTOInMap(JoularNodeKeyHashMap key) {
        return this.joularNodeEntityDTOMap.containsKey(key);
    }

    public JoularNodeEntityDTO getJoularNodeEntityDTO(JoularNodeKeyHashMap key) {
        return joularNodeEntityDTOMap.get(key);
    }

    public Set<JoularNodeKeyHashMap> getKeys() {
        return joularNodeEntityDTOMap.keySet();
    }

    @Override
    public String toString() {
        return "JoularNodeHashMapDTO{" +
            "joularNodeEntityDTOMap=" + joularNodeEntityDTOMap +
            '}';
    }
}
