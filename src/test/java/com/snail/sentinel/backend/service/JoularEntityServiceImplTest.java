package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.repository.*;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.impl.CkEntityServiceImpl;
import com.snail.sentinel.backend.service.impl.CommitEntityServiceImpl;
import com.snail.sentinel.backend.service.impl.JoularEntityServiceImpl;
import com.snail.sentinel.backend.service.mapper.CkEntityMapper;
import com.snail.sentinel.backend.service.mapper.JoularEntityMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class JoularEntityServiceImplTest {

    private final Logger log = LoggerFactory.getLogger(JoularEntityServiceImplTest.class);

    @Mock
    private CkEntityRepository ckEntityRepository;

    @Mock
    private CkEntityMapper ckEntityMapper;

    @InjectMocks
    private CkEntityServiceImpl ckService;

    @Mock
    private CommitEntityRepository commitEntityRepository;

    @InjectMocks
    private CommitEntityServiceImpl commitEntityServiceImpl;

    @Mock
    private JoularEntityRepository joularEntityRepository;

    @Mock
    private JoularEntityMapper joularEntityMapper;

    @InjectMocks
    private JoularEntityServiceImpl joularService;

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

        ckEntityRepository = Mockito.mock(CkEntityRepository.class);
        ckService = new CkEntityServiceImpl(ckEntityRepository, ckEntityMapper);

        joularEntityRepository = Mockito.mock(JoularEntityRepository.class);
        joularService = new JoularEntityServiceImpl(joularEntityRepository, joularEntityMapper);

        commitEntityRepository = Mockito.mock(CommitEntityRepository.class);
        commitEntityServiceImpl = new CommitEntityServiceImpl(commitEntityRepository);
    }

    @Test
    void simpleGetClassMethodLineTest() {
        String line = "org.apache.commons.configuration2.TestINIConfiguration.testValueWithSemicolon 1006";
        JSONObject maybeClassMethodLine = joularService.getClassMethodLine(line);

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.apache.commons.configuration2.TestINIConfiguration");
        classMethodLine.put("methodName", "testValueWithSemicolon");
        classMethodLine.put("lineNumber", 1006);

        assertTrue(maybeClassMethodLine.similar(classMethodLine));
    }

    @Test
    void simple2GetClassMethodLineTest() {
        JSONObject maybeClassMethodLine = joularService.getClassMethodLine("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 60");

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        classMethodLine.put("methodName", "getFileNames");
        classMethodLine.put("lineNumber", 60);

        assertTrue(maybeClassMethodLine.similar(classMethodLine));
    }

    /*@Test
    void complexGetClassMethodLineTest() {
        String line = "org.apache.commons.configuration2.builder.BasicConfigurationBuilder.lambda$getFilteredParameters$0";
        JSONObject maybeClassMethodLine = joularService.getClassMethodLine(line);

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.apache.commons.configuration2.builder.BasicConfigurationBuilder");
        classMethodLine.put("methodName", "");
    }*/

    @Test
    void simpleGetMatchCkJoularTest() {
        JSONObject classMethodLine = joularService.getClassMethodLine("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 67");
        CkAggregateLineDTO maybeCkAggregateLineDTO = joularService.getMatchCkJoular(classMethodLine, ckAggregateLineHashMapDTO);

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
        JSONObject classMethodLine = joularService.getClassMethodLine("org.apache.commons.configuration2.tree.DefaultConfigurationKey$KeyIterator.nextDelimiterPos 662");
        CkAggregateLineDTO maybeCkAggregateLineDTO = joularService.getMatchCkJoular(classMethodLine, ckAggregateLineHashMapDTO);

        CkAggregateLineDTO ckAggregateLineDTO = new CkAggregateLineDTO();
        ckAggregateLineDTO.setClassName("org.apache.commons.configuration2.tree.DefaultConfigurationKey");
        ckAggregateLineDTO.setMethodName("nextDelimiterPos/1");
        ckAggregateLineDTO.setFilePath("filePath2");
        ckAggregateLineDTO.setLine(new ArrayList<>(){{add(660);}});
        ckAggregateLineDTO.setLoc(new ArrayList<>(){{add(10);}});

        assertEquals(maybeCkAggregateLineDTO, ckAggregateLineDTO);
    }

    /*@Test
    void createJoularEntityDTOList() throws Exception {
        HashMap<String, String> repoItem = new HashMap<>();
        repoItem.put(Util.OWNER, "spring-projects");
        repoItem.put(Util.NAME, "spring-boot");
        repoItem.put(Util.SHA, "9edc7723129ae3c56db332691c0d1d49db7d32d0");
        repoItem.put(Util.COMPLEXITY, "complex");

        CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO = ckService.aggregate("spring-boot");
        log.info("ckAggregateLineHashMapDTO = {}", ckAggregateLineHashMapDTO);

        JSONObject commitData = commitService.getCommitData(repoItem.get(Util.OWNER), repoItem.get(Util.NAME), repoItem.get(Util.SHA));
        CommitCompleteDTO commitCompleteDTO = commitService.createCommitEntityDTO(repoItem, commitData);

        String iterationPath = "src/test/resources/joular-csv-test/";

        JoularEntityListDTO maybeJoularEntityListDTO = joularService.createJoularEntityDTOList(ckAggregateLineHashMapDTO, commitCompleteDTO, iterationPath);


        JoularEntityListDTO expectedJoularEntityListDTO = new JoularEntityListDTO();

        JoularEntityDTO expectedEntityDTOOne = new JoularEntityDTO();
        IterationDTO iterationDTOOne = new IterationDTO(242090, parseLong("1694704203901"));
        CommitSimpleDTO commitSimpleDTO = Util.createCommitSimpleFromCommitCompleteDTO(commitCompleteDTO);

        JSONObject classMethodLineOne = new JSONObject();
        classMethodLineOne.put("methodName","getFileNames");
        classMethodLineOne.put("className", "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        classMethodLineOne.put("lineNumber", 64);


        CkAggregateLineDTO matchedCkJoularOne = joularService.getMatchCkJoular(classMethodLineOne, ckAggregateLineHashMapDTO);
        MethodElementDTO methodElementDTOOne = (MethodElementDTO) Util.getMeasurableElement("method", matchedCkJoularOne);

        expectedEntityDTOOne.setIterationDTO(iterationDTOOne);
        expectedEntityDTOOne.setCommitSimpleDTO(commitSimpleDTO);
        expectedEntityDTOOne.setScope("app");
        expectedEntityDTOOne.setMonitoringType("total");
        expectedEntityDTOOne.setValue(0.3167F);
        expectedEntityDTOOne.setMethodElementDTO(methodElementDTOOne);


        JoularEntityDTO expectedEntityDTOTwo = new JoularEntityDTO();
        IterationDTO iterationDTOTwo = new IterationDTO(424172, parseLong("1694770609086"));

        JSONObject classMethodLineTwo = new JSONObject();
        classMethodLineTwo.put("methodName","loadClass");
        classMethodLineTwo.put("className", "org.springframework.boot.testsupport.classpath.ModifiedClassPathClassLoader");
        classMethodLineTwo.put("lineNumber", 98);

        CkAggregateLineDTO matchedCkJoularTwo = joularService.getMatchCkJoular(classMethodLineTwo, ckAggregateLineHashMapDTO);
        MethodElementDTO methodElementDTOTwo = (MethodElementDTO) Util.getMeasurableElement("method", matchedCkJoularTwo);

        expectedEntityDTOTwo.setIterationDTO(iterationDTOTwo);
        expectedEntityDTOTwo.setCommitSimpleDTO(commitSimpleDTO);
        expectedEntityDTOTwo.setScope("app");
        expectedEntityDTOTwo.setMonitoringType("total");
        expectedEntityDTOTwo.setValue(26.8692F);
        expectedEntityDTOTwo.setMethodElementDTO(methodElementDTOTwo);

        expectedJoularEntityListDTO.add(expectedEntityDTOOne);
        expectedJoularEntityListDTO.add(expectedEntityDTOTwo);

        assertEquals(maybeJoularEntityListDTO, expectedJoularEntityListDTO);
    }*/
}
