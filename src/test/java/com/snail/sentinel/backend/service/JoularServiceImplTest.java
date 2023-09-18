package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.repository.CkEntityRepositoryAggregationImpl;
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
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import static java.lang.Long.parseLong;
import static org.junit.jupiter.api.Assertions.*;

class JoularServiceImplTest {
    @Mock
    private CkEntityRepository ckEntityRepository;
    @Mock
    private CkEntityRepositoryAggregationImpl ckEntityRepositoryAggregationImpl;

    @Mock
    private CkEntityMapper ckEntityMapper;

    @InjectMocks
    private CkServiceImpl ckService;

    @InjectMocks
    private CommitService commitService;

    @InjectMocks
    private JoularServiceImpl joularService;

    @BeforeEach
    public void init() {
        ckEntityRepository = Mockito.mock(CkEntityRepository.class);
        ckService = new CkServiceImpl(ckEntityRepository, ckEntityMapper, ckEntityRepositoryAggregationImpl);
    }

    @Test
    void createJoularEntityDTOList() throws Exception {
        HashMap<String, String> repoItem = new HashMap<>();
        repoItem.put(Util.OWNER, "spring-projects");
        repoItem.put(Util.NAME, "spring-boot");
        repoItem.put(Util.SHA, "9edc7723129ae3c56db332691c0d1d49db7d32d0");
        repoItem.put(Util.COMPLEXITY, "complex");

        CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO = ckService.aggregate("spring-boot");

        JSONObject commitData = commitService.getCommitData(repoItem.get(Util.OWNER), repoItem.get(Util.NAME), repoItem.get(Util.SHA));
        CommitCompleteDTO commitCompleteDTO = commitService.createCommitEntityDTO(repoItem, commitData);

        String iterationPath = "src/test/resources/joular-csv-test/app/total/methods/";

        JoularEntityListDTO maybeJoularEntityListDTO = joularService.createJoularEntityDTOList(ckAggregateLineHashMapDTO, commitCompleteDTO, iterationPath);


        JoularEntityListDTO expectedJoularEntityListDTO = new JoularEntityListDTO();

        JoularEntityDTO expectedEntityDTOOne = new JoularEntityDTO();
        IterationDTO iterationDTOOne = new IterationDTO(242090, parseLong("1694704203901"));
        CommitSimpleDTO commitSimpleDTO = Util.createCommitSimpleFromCommitCompleteDTO(commitCompleteDTO);

        JSONObject classMethodLineOne = new JSONObject();
        classMethodLineOne.put("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 64", "0.3167");

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
        classMethodLineTwo.put("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 64", "1.032");

        CkAggregateLineDTO matchedCkJoularTwo = joularService.getMatchCkJoular(classMethodLineTwo, ckAggregateLineHashMapDTO);
        MethodElementDTO methodElementDTOTwo = (MethodElementDTO) Util.getMeasurableElement("method", matchedCkJoularTwo);

        expectedEntityDTOTwo.setIterationDTO(iterationDTOTwo);
        expectedEntityDTOTwo.setCommitSimpleDTO(commitSimpleDTO);
        expectedEntityDTOTwo.setScope("app");
        expectedEntityDTOTwo.setMonitoringType("total");
        expectedEntityDTOTwo.setValue(1.032F);
        expectedEntityDTOTwo.setMethodElementDTO(methodElementDTOTwo);

        expectedJoularEntityListDTO.add(expectedEntityDTOOne);
        expectedJoularEntityListDTO.add(expectedEntityDTOTwo);

        assertThat(maybeJoularEntityListDTO).isEqualTo(expectedJoularEntityListDTO);
    }
}
