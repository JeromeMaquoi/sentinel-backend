package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.service.dto.joularnode.JoularNodeHashMapDTO;
import com.snail.sentinel.backend.service.dto.joularnode.JoularNodeKeyHashMap;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JoularNodeHashMapDTOTest {

    @Test
    void trueIsJoularNodeEntityDTOInMap() {
        JoularNodeKeyHashMap joularNodeKeyHashMap = new JoularNodeKeyHashMap("classMethodSignature",1, new ArrayList<>());
        JoularNodeEntityDTO joularNodeEntityDTO = mock(JoularNodeEntityDTO.class);
        MeasurableElementDTO measurableElementDTO = mock(MeasurableElementDTO.class);
        when(joularNodeEntityDTO.getLineNumber()).thenReturn(1);
        when(joularNodeEntityDTO.getMeasurableElement()).thenReturn(measurableElementDTO);
        when(measurableElementDTO.getClassMethodSignature()).thenReturn("classMethodSignature");
        when(joularNodeEntityDTO.getAncestors()).thenReturn(new ArrayList<>());

        JoularNodeHashMapDTO joularNodeHashMapDTO = new JoularNodeHashMapDTO();
        joularNodeHashMapDTO.insertOne(joularNodeEntityDTO);

        boolean isJoularNodeEntityDTOInMap = joularNodeHashMapDTO.isJoularNodeEntityDTOInMap(joularNodeKeyHashMap);
        assertTrue(isJoularNodeEntityDTOInMap);
    }
}
