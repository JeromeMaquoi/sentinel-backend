package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CkAggregateLineHashMapDTOTest {
    private final Logger log = LoggerFactory.getLogger(CkAggregateLineHashMapDTOTest.class);

    private CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO;

    @BeforeEach
    public void init() {
        ckAggregateLineHashMapDTO = new CkAggregateLineHashMapDTO();

        CkAggregateLineDTO ckAggregateLineDTO1 = new CkAggregateLineDTO();
        ckAggregateLineDTO1.setClassName("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        ckAggregateLineDTO1.setFilePath("filePath");
        ckAggregateLineDTO1.setMethodName("getFileNames/2");
        ckAggregateLineDTO1.setLine(new ArrayList<>(){{add(63);}});
        ckAggregateLineDTO1.setLoc(new ArrayList<>(){{add(5);}});

        CkAggregateLineDTO ckAggregateLineDTO2 = new CkAggregateLineDTO();
        ckAggregateLineDTO2.setClassName("org.apache.commons.configuration2.tree.DefaultConfigurationKey");
        ckAggregateLineDTO2.setFilePath("filePath2");
        ckAggregateLineDTO2.setMethodName("nextDelimiterPos/1");
        ckAggregateLineDTO2.setLine(new ArrayList<>(){{add(660);}});
        ckAggregateLineDTO2.setLoc(new ArrayList<>(){{add(10);}});

        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO1);
        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO2);
    }

    @Test
    void getAllOccurrencesSimpleTest() {
        String className = "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints";
        String methodName = "getFileNames/2";
        List<CkAggregateLineDTO> maybeAllOccurrences = ckAggregateLineHashMapDTO.getAllOccurrences(className, methodName);

        CkAggregateLineDTO ckAggregateLineDTO = new CkAggregateLineDTO();
        ckAggregateLineDTO.setClassName("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        ckAggregateLineDTO.setFilePath("filePath");
        ckAggregateLineDTO.setMethodName("getFileNames/2");
        ckAggregateLineDTO.setLine(new ArrayList<>(){{add(63);}});
        ckAggregateLineDTO.setLoc(new ArrayList<>(){{add(5);}});
        List<CkAggregateLineDTO> allOccurrences = new ArrayList<>();
        allOccurrences.add(ckAggregateLineDTO);

        assertEquals(maybeAllOccurrences, allOccurrences);
    }

    @Test
    void getAllOccurrencesEmptyTest() {
        String className = "class.not.in.hash.map";
        String methodName = "getFileNames/2";
        List<CkAggregateLineDTO> maybeAllOccurrences = ckAggregateLineHashMapDTO.getAllOccurrences(className, methodName);

        List<CkAggregateLineDTO> allOccurrences = new ArrayList<>();

        assertEquals(maybeAllOccurrences, allOccurrences);
    }
}
