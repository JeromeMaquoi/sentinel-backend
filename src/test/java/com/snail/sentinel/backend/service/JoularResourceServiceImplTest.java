package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.repository.CkEntityRepositoryAggregation;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.exceptions.GetMatchCkJoularException;
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

class JoularResourceServiceImplTest {
    @Mock
    private CommitEntityService commitEntityService;
    @Mock
    private CkEntityRepositoryAggregation ckEntityRepositoryAggregation;
    @InjectMocks
    private JoularResourceServiceImpl joularResourceService;

    private CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO;

    private String className;

    private CkAggregateLineDTO ckAggregateLineDTO2;

    private CkAggregateLineDTO ckAggregateLineDTO3;

    private CkAggregateLineDTO ckAggregateLineDTO4;

    @BeforeEach
    public void init() {
        commitEntityService = Mockito.mock(CommitEntityService.class);
        ckEntityRepositoryAggregation = Mockito.mock(CkEntityRepositoryAggregation.class);
        joularResourceService = new JoularResourceServiceImpl(commitEntityService, ckEntityRepositoryAggregation);

        className = "org.apache.commons.configuration2.tree.DefaultConfigurationKey";
        String filePath = "filePath2";

        ckAggregateLineHashMapDTO = new CkAggregateLineHashMapDTO();
        CkAggregateLineDTO ckAggregateLineDTO1 = new CkAggregateLineDTO();
        ckAggregateLineDTO1.setClassName("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        ckAggregateLineDTO1.setFilePath("filePath");
        ckAggregateLineDTO1.setMethodName("getFileNames/2");
        ckAggregateLineDTO1.setLine(new ArrayList<>(){{add(63);}});
        ckAggregateLineDTO1.setLoc(new ArrayList<>(){{add(5);}});

        ckAggregateLineDTO2 = new CkAggregateLineDTO();
        ckAggregateLineDTO2.setClassName(className);
        ckAggregateLineDTO2.setFilePath(filePath);
        ckAggregateLineDTO2.setMethodName("nextDelimiterPos/1[String]");
        ckAggregateLineDTO2.setLine(new ArrayList<>(){{add(660);}});
        ckAggregateLineDTO2.setLoc(new ArrayList<>(){{add(10);}});

        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO1);
        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO2);
        joularResourceService.setCkAggregateLineHashMapDTO(ckAggregateLineHashMapDTO);

        ckAggregateLineDTO3 = new CkAggregateLineDTO();
        ckAggregateLineDTO3.setClassName(className);
        ckAggregateLineDTO3.setFilePath(filePath);
        ckAggregateLineDTO3.setMethodName("nextDelimiterPos/0");
        ckAggregateLineDTO3.setLine(new ArrayList<>(){{add(650);}});
        ckAggregateLineDTO3.setLoc(new ArrayList<>(){{add(8);}});

        ckAggregateLineDTO4 = new CkAggregateLineDTO();
        ckAggregateLineDTO4.setClassName(className);
        ckAggregateLineDTO4.setFilePath(filePath);
        ckAggregateLineDTO4.setMethodName("nextDelimiterPos/2[String, String]");
        ckAggregateLineDTO4.setLine(new ArrayList<>(){{add(680);}});
        ckAggregateLineDTO4.setLoc(new ArrayList<>(){{add(10);}});
    }

    @Test
    void simpleGetClassMethodLineTest() {
        String line = "org.apache.commons.configuration2.TestINIConfiguration.testValueWithSemicolon 1006";
        Optional<JSONObject> optionalResult = joularResourceService.getClassMethodLine(line);
        assertTrue(optionalResult.isPresent());
        JSONObject maybeClassMethodLine = optionalResult.orElseThrow(() -> new IllegalStateException("Class method line not found for " + line));

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.apache.commons.configuration2.TestINIConfiguration");
        classMethodLine.put("methodName", "testValueWithSemicolon");
        classMethodLine.put("lineNumber", 1006);

        assertTrue(maybeClassMethodLine.similar(classMethodLine));
    }

    @Test
    void simple2GetClassMethodLineTest() {
        String line = "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 60";
        Optional<JSONObject> optionalResult = joularResourceService.getClassMethodLine(line);
        assertTrue(optionalResult.isPresent());
        JSONObject maybeClassMethodLine = optionalResult.orElseThrow(() -> new IllegalStateException("Class method line not found for " + line));

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        classMethodLine.put("methodName", "getFileNames");
        classMethodLine.put("lineNumber", 60);

        assertTrue(maybeClassMethodLine.similar(classMethodLine));
    }

    @Test
    void emptyGetClassMethodLineTest() {
        Optional<JSONObject> optionalResult = joularResourceService.getClassMethodLine("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 60 59 46");
        assertTrue(optionalResult.isEmpty());
    }

    @Test
    void simpleGetMatchCkJoularTest() {
        String line = "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 67";
        Optional<CkAggregateLineDTO> optionalMaybeCkAggregateLineDTO = joularResourceService.getMatchCkJoular(line);
        assertTrue(optionalMaybeCkAggregateLineDTO.isPresent());
        CkAggregateLineDTO maybeCkAggregateLineDTO = optionalMaybeCkAggregateLineDTO.orElseThrow(() -> new GetMatchCkJoularException(line));

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
        CkAggregateLineDTO maybeCkAggregateLineDTO = optionalMaybeCkAggregateLineDTO.orElseThrow(() -> new GetMatchCkJoularException(line));

        CkAggregateLineDTO ckAggregateLineDTO = new CkAggregateLineDTO();
        ckAggregateLineDTO.setClassName("org.apache.commons.configuration2.tree.DefaultConfigurationKey");
        ckAggregateLineDTO.setMethodName("nextDelimiterPos/1[String]");
        ckAggregateLineDTO.setFilePath("filePath2");
        ckAggregateLineDTO.setLine(new ArrayList<>(){{add(660);}});
        ckAggregateLineDTO.setLoc(new ArrayList<>(){{add(10);}});

        assertEquals(ckAggregateLineDTO, maybeCkAggregateLineDTO);
    }

    @Test
    void simpleFindOccFromMultipleOccTest() {
        List<CkAggregateLineDTO> allOccurrences = new ArrayList<>();
        allOccurrences.add(ckAggregateLineDTO2);
        allOccurrences.add(ckAggregateLineDTO3);
        String methodName = "nextDelimiterPos";
        int numberLine = 675;

        Optional<CkAggregateLineDTO> optionalMaybeGoodOccurrence = joularResourceService.findOccFromMultipleOcc(allOccurrences, className, methodName, numberLine);
        assertTrue(optionalMaybeGoodOccurrence.isPresent());
        CkAggregateLineDTO maybeGoodOccurrence = optionalMaybeGoodOccurrence.orElseThrow(() -> new IllegalStateException("Good occurrence is not present"));

        assertEquals(ckAggregateLineDTO2, maybeGoodOccurrence);
    }

    @Test
    void threeOccListFindOccFromMultipleOccTest() {
        List<CkAggregateLineDTO> allOccurrences = new ArrayList<>();
        allOccurrences.add(ckAggregateLineDTO2);
        allOccurrences.add(ckAggregateLineDTO3);
        allOccurrences.add(ckAggregateLineDTO4);
        String methodName = "nextDelimiterPos";
        int numberLine = 679;

        Optional<CkAggregateLineDTO> optionalMaybeGoodOccurrence = joularResourceService.findOccFromMultipleOcc(allOccurrences, className, methodName, numberLine);
        assertTrue(optionalMaybeGoodOccurrence.isPresent());
        CkAggregateLineDTO maybeGoodOccurrence = optionalMaybeGoodOccurrence.orElseThrow(() -> new IllegalStateException("Occurrence from multiple occurrence is not present"));

        assertEquals(ckAggregateLineDTO2, maybeGoodOccurrence);
    }

    @Test
    void emptyFindOccFromMultipleOccTest() {
        List<CkAggregateLineDTO> allOccurrences = new ArrayList<>();
        allOccurrences.add(ckAggregateLineDTO2);

        String methodName = "nextDelimiterPos";
        int numberLine = 650;

        Optional<CkAggregateLineDTO> optionalMaybeGoodOccurrence = joularResourceService.findOccFromMultipleOcc(allOccurrences, className, methodName, numberLine);
        assertTrue(optionalMaybeGoodOccurrence.isEmpty());
    }

    @Test
    void goodLinesFindOccFromMultipleOccTest() {
        List<CkAggregateLineDTO> allOccurrences = new ArrayList<>();
        allOccurrences.add(ckAggregateLineDTO2);
        allOccurrences.add(ckAggregateLineDTO3);
        allOccurrences.add(ckAggregateLineDTO4);

        String methodName = "nextDelimiterPos";
        int numberLine = 655;

        Optional<CkAggregateLineDTO> optionalMaybeGoodOccurrence = joularResourceService.findOccFromMultipleOcc(allOccurrences, className, methodName, numberLine);
        assertTrue(optionalMaybeGoodOccurrence.isPresent());
        CkAggregateLineDTO maybeGoodOccurrence = optionalMaybeGoodOccurrence.orElseThrow(() -> new IllegalStateException("Occurrence from multiple occurrence is not present"));
        assertEquals(ckAggregateLineDTO3, maybeGoodOccurrence);
    }

    @Test
    void multipleOccFindGoodOccurrence() {
        List<CkAggregateLineDTO> allOccurrences = new ArrayList<>();
        allOccurrences.add(ckAggregateLineDTO2);
        allOccurrences.add(ckAggregateLineDTO3);

        String methodName = "nextDelimiterPos";
        int numberLine = 675;

        Optional<CkAggregateLineDTO> optionalMaybeGoodOccurrence = joularResourceService.findGoodOccurrence(allOccurrences, className, methodName, numberLine);
        assertTrue(optionalMaybeGoodOccurrence.isPresent());
        CkAggregateLineDTO maybeGoodOccurrence = optionalMaybeGoodOccurrence.orElseThrow(() -> new IllegalStateException("Good occurrence is not present"));
        assertEquals(ckAggregateLineDTO2, maybeGoodOccurrence);
    }
}
