package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.repository.CkEntityRepositoryAggregation;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.impl.JoularResourceServiceImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class JoularResourceServiceImplTest {
    @Mock
    private CommitEntityService commitEntityService;
    @Mock
    private CkEntityRepositoryAggregation ckEntityRepositoryAggregation;
    @InjectMocks
    private JoularResourceServiceImpl joularResourceService;

    private CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO;

    @BeforeEach
    public void init() {
        commitEntityService = Mockito.mock(CommitEntityService.class);
        ckEntityRepositoryAggregation = Mockito.mock(CkEntityRepositoryAggregation.class);
        joularResourceService = new JoularResourceServiceImpl(commitEntityService, ckEntityRepositoryAggregation);

        ckAggregateLineHashMapDTO = new CkAggregateLineHashMapDTO();
        CkAggregateLineDTO ckAggregateLineDTO1 = new CkAggregateLineDTO();
        ckAggregateLineDTO1.setClassName("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        ckAggregateLineDTO1.setFilePath("filePath");
        ckAggregateLineDTO1.setMethodName("getFileNames/2");
        ckAggregateLineDTO1.setLine(new ArrayList<>(){{add(63);}});
        ckAggregateLineDTO1.setLoc(new ArrayList<>(){{add(5);}});

        List<CkAggregateLineDTO> allOccurrencesSimple = new ArrayList<>();
        allOccurrencesSimple.add(ckAggregateLineDTO1);

        when(ckEntityRepositoryAggregation.aggregateClassMethod("commons-configuration", "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints", "getFileNames")).thenReturn(allOccurrencesSimple);

        CkAggregateLineDTO ckAggregateLineDTO2 = new CkAggregateLineDTO();
        ckAggregateLineDTO2.setClassName("org.apache.commons.configuration2.tree.DefaultConfigurationKey");
        ckAggregateLineDTO2.setFilePath("filePath2");
        ckAggregateLineDTO2.setMethodName("nextDelimiterPos/1[String]");
        ckAggregateLineDTO2.setLine(new ArrayList<>(){{add(660);}});
        ckAggregateLineDTO2.setLoc(new ArrayList<>(){{add(10);}});

        List<CkAggregateLineDTO> allOccurrencesComplex = new ArrayList<>();
        allOccurrencesComplex.add(ckAggregateLineDTO2);

        when(ckEntityRepositoryAggregation.aggregateClassMethod("commons-configuration", "org.apache.commons.configuration2.tree.DefaultConfigurationKey", "nextDelimiterPos")).thenReturn(allOccurrencesComplex);

        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO1);
        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO2);
        joularResourceService.setCkAggregateLineHashMapDTO(ckAggregateLineHashMapDTO);
        joularResourceService.setRepoName("commons-configuration");
    }

    @Test
    void simpleGetClassMethodLineTest() {
        String line = "org.apache.commons.configuration2.TestINIConfiguration.testValueWithSemicolon 1006";
        Optional<JSONObject> optionalResult = joularResourceService.getClassMethodLine(line);
        assertTrue(optionalResult.isPresent());
        JSONObject maybeClassMethodLine = optionalResult.get();

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.apache.commons.configuration2.TestINIConfiguration");
        classMethodLine.put("methodName", "testValueWithSemicolon");
        classMethodLine.put("lineNumber", 1006);

        assertTrue(maybeClassMethodLine.similar(classMethodLine));
    }

    @Test
    void simple2GetClassMethodLineTest() {
        Optional<JSONObject> optionalResult = joularResourceService.getClassMethodLine("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 60");
        assertTrue(optionalResult.isPresent());
        JSONObject maybeClassMethodLine = optionalResult.get();

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        classMethodLine.put("methodName", "getFileNames");
        classMethodLine.put("lineNumber", 60);

        assertTrue(maybeClassMethodLine.similar(classMethodLine));
    }

    @Test
    void simpleGetMatchCkJoularTest() {
        String line = "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 67";
        Optional<CkAggregateLineDTO> optionalMaybeCkAggregateLineDTO = joularResourceService.getMatchCkJoular(line);
        assertTrue(optionalMaybeCkAggregateLineDTO.isPresent());
        CkAggregateLineDTO maybeCkAggregateLineDTO = optionalMaybeCkAggregateLineDTO.get();

        CkAggregateLineDTO ckAggregateLineDTO = new CkAggregateLineDTO();
        ckAggregateLineDTO.setClassName("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        ckAggregateLineDTO.setMethodName("getFileNames/2");
        ckAggregateLineDTO.setFilePath("filePath");
        ckAggregateLineDTO.setLine(new ArrayList<>(){{add(63);}});
        ckAggregateLineDTO.setLoc(new ArrayList<>(){{add(5);}});

        assertEquals(maybeCkAggregateLineDTO, ckAggregateLineDTO);
    }

    @Test
    void complexGetMatchCkJoularTest() {
        String line = "org.apache.commons.configuration2.tree.DefaultConfigurationKey$KeyIterator.nextDelimiterPos 662";
        Optional<CkAggregateLineDTO> optionalMaybeCkAggregateLineDTO = joularResourceService.getMatchCkJoular(line);
        assertTrue(optionalMaybeCkAggregateLineDTO.isPresent());
        CkAggregateLineDTO maybeCkAggregateLineDTO = optionalMaybeCkAggregateLineDTO.get();

        CkAggregateLineDTO ckAggregateLineDTO = new CkAggregateLineDTO();
        ckAggregateLineDTO.setClassName("org.apache.commons.configuration2.tree.DefaultConfigurationKey");
        ckAggregateLineDTO.setMethodName("nextDelimiterPos/1[String]");
        ckAggregateLineDTO.setFilePath("filePath2");
        ckAggregateLineDTO.setLine(new ArrayList<>(){{add(660);}});
        ckAggregateLineDTO.setLoc(new ArrayList<>(){{add(10);}});

        assertEquals(maybeCkAggregateLineDTO, ckAggregateLineDTO);
    }
}
