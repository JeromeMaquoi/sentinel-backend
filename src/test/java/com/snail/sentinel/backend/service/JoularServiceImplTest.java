package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.repository.*;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityListDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MethodElementDTO;
import com.snail.sentinel.backend.service.impl.CkServiceImpl;
import com.snail.sentinel.backend.service.impl.JoularServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Long.parseLong;
import static org.mockito.Mockito.when;

class JoularServiceImplTest {

    private final Logger log = LoggerFactory.getLogger(JoularServiceImplTest.class);

    @Mock
    private CkEntityRepository ckEntityRepository;

    @Mock
    private CkEntityRepositoryAggregationImpl ckEntityRepositoryAggregationImpl;

    @Mock
    private CkEntityMapper ckEntityMapper;

    @InjectMocks
    private CkServiceImpl ckService;

    @Mock
    private CommitEntityRepository commitEntityRepository;

    @InjectMocks
    private CommitService commitService;

    @Mock
    private JoularEntityRepository joularEntityRepository;

    @Mock
    private JoularEntityMapper joularEntityMapper;

    @InjectMocks
    private JoularServiceImpl joularService;

    @BeforeEach
    public void init() {
        CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO = new CkAggregateLineHashMapDTO();
        CkAggregateLineDTO ckAggregateLineDTO1 = new CkAggregateLineDTO();
        ckAggregateLineDTO1.setClassName("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        ckAggregateLineDTO1.setFilePath("filePath");
        ckAggregateLineDTO1.setMethodSignature("getFileNames(arg1, arg2)");
        ckAggregateLineDTO1.setLine(new ArrayList<>(){{add(63);}});
        ckAggregateLineDTO1.setLoc(new ArrayList<>(){{add(5);}});

        CkAggregateLineDTO ckAggregateLineDTO2 = new CkAggregateLineDTO();
        ckAggregateLineDTO2.setClassName("org.springframework.boot.testsupport.classpath.ModifiedClassPathClassLoader");
        ckAggregateLineDTO2.setFilePath("filePath2");
        ckAggregateLineDTO2.setMethodSignature("loadClass(arg1)");
        ckAggregateLineDTO2.setLine(new ArrayList<>(){{add(90);}});
        ckAggregateLineDTO2.setLoc(new ArrayList<>(){{add(15);}});

        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO1);
        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO2);

        ckEntityRepository = Mockito.mock(CkEntityRepository.class);
        ckEntityRepositoryAggregationImpl = Mockito.mock(CkEntityRepositoryAggregationImpl.class);
        when(ckEntityRepositoryAggregationImpl.aggregate(Mockito.anyString())).thenReturn(ckAggregateLineHashMapDTO);
        ckService = new CkServiceImpl(ckEntityRepository, ckEntityMapper, ckEntityRepositoryAggregationImpl);

        joularEntityRepository = Mockito.mock(JoularEntityRepository.class);
        joularService = new JoularServiceImpl(joularEntityRepository, joularEntityMapper);

        commitEntityRepository = Mockito.mock(CommitEntityRepository.class);
        commitService = new CommitService(commitEntityRepository);
    }

    @Test
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
    }
}
