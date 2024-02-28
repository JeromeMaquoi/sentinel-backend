package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.commons.FileListProvider;
import com.snail.sentinel.backend.commons.TestingFileListProvider;
import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.repository.*;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.commit.StatsDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityListDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MethodElementSetDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositoryCompleteDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositorySimpleDTO;
import com.snail.sentinel.backend.service.impl.CkEntityServiceImpl;
import com.snail.sentinel.backend.service.impl.CommitEntityServiceImpl;
import com.snail.sentinel.backend.service.impl.JoularEntityServiceImpl;
import com.snail.sentinel.backend.service.impl.JoularResourceServiceImpl;
import com.snail.sentinel.backend.service.mapper.CkEntityMapper;
import com.snail.sentinel.backend.service.mapper.JoularEntityMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


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

    @Mock
    private CkEntityRepositoryAggregation ckEntityRepositoryAggregation;

    @Mock
    private CommitEntityService commitEntityService;

    @InjectMocks
    private JoularEntityServiceImpl joularEntityService;

    private JoularResourceService joularResourceService;

    private CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO;

    private CommitCompleteDTO commitCompleteDTO;

    private RepositoryCompleteDTO repository;

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

        createCompleteCommitDTO();

        ckEntityRepository = Mockito.mock(CkEntityRepository.class);
        ckService = new CkEntityServiceImpl(ckEntityRepository, ckEntityMapper);

        joularEntityRepository = Mockito.mock(JoularEntityRepository.class);
        ckEntityRepositoryAggregation = Mockito.mock(CkEntityRepositoryAggregation.class);
        commitEntityService = Mockito.mock(CommitEntityService.class);
        joularResourceService = new JoularResourceServiceImpl(commitEntityService, ckEntityRepositoryAggregation);
        joularResourceService.setCkAggregateLineHashMapDTO(ckAggregateLineHashMapDTO);
        joularEntityService = new JoularEntityServiceImpl(joularEntityRepository, joularEntityMapper, joularResourceService);

        when(ckEntityRepositoryAggregation.aggregate("commons-configuration")).thenReturn(ckAggregateLineHashMapDTO);
        //joularEntityService.setCkAggregateLineHashMapDTO("commons-configuration");

        commitEntityRepository = Mockito.mock(CommitEntityRepository.class);
        commitEntityServiceImpl = new CommitEntityServiceImpl(commitEntityRepository);
    }

    private void createCompleteCommitDTO() {
        List<String> parentsSha = new ArrayList<>();
        parentsSha.add("f540433112b9a93c26c43277c3ec7a3d40565115");
        parentsSha.add("41823fe1509c84324a975297bc09e0d884e1c2e9");

        this.repository = new RepositoryCompleteDTO();
        this.repository.setName("commons-configuration");
        this.repository.setOwner("apache");
        this.repository.setUrl("https://github.com/apache/commons-configuration");

        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setAdditions(183);
        statsDTO.setDeletions(102);

        this.commitCompleteDTO = new CommitCompleteDTO();
        this.commitCompleteDTO.setSha("59e5152722198526c6ffe5361de7d1a6a87275c7");
        this.commitCompleteDTO.setDate("2022-06-30T03:54:57Z");
        this.commitCompleteDTO.setMessage("merging doc updates from master");
        this.commitCompleteDTO.setParentsSha(parentsSha);
        this.commitCompleteDTO.setRepository(repository);
        this.commitCompleteDTO.setStatsDTO(statsDTO);
    }

    /*@Test
    void createJoularEntityDTOListForOneIterationTest() {
        Path iterationDirPath = Paths.get("joular-csv-test/1-1326858-1701080339565");

        FileListProvider fileListProvider = new TestingFileListProvider();

        JoularEntityListDTO maybeJoularEntityListDTO = joularEntityService.createJoularEntityDTOListForOneIteration(iterationDirPath, commitCompleteDTO, fileListProvider);


        MeasurableElementDTO methodElementDTO = new MeasurableElementDTO();
        methodElementDTO.setAstElem("method");
        methodElementDTO.setFilePath("filePath");
        methodElementDTO.setClassName("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        methodElementDTO.setMethodName("getFileNames/2");
        methodElementDTO.setClassMethodSignature("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames");

        IterationDTO iterationDTO = new IterationDTO();
        iterationDTO.setIterationId(1);
        iterationDTO.setPid(1326858);
        iterationDTO.setStartTimestamp(1701080339565L);

        CommitSimpleDTO commitSimpleDTO = new CommitSimpleDTO();
        RepositorySimpleDTO repositorySimpleDTO = new RepositorySimpleDTO();
        repositorySimpleDTO.setOwner(repository.getOwner());
        repositorySimpleDTO.setName(repository.getName());
        commitSimpleDTO.setRepository(repositorySimpleDTO);
        commitSimpleDTO.setSha("59e5152722198526c6ffe5361de7d1a6a87275c7");


        JoularEntityDTO joularEntityDTO = new JoularEntityDTO();
        joularEntityDTO.setValue(107.43F);
        joularEntityDTO.setScope("app");
        joularEntityDTO.setMonitoringType("total");
        joularEntityDTO.setMethodElementDTO(methodElementDTO);
        joularEntityDTO.setIterationDTO(iterationDTO);
        joularEntityDTO.setCommitSimpleDTO(commitSimpleDTO);

        JoularEntityListDTO joularEntityListDTO = new JoularEntityListDTO();
        joularEntityListDTO.add(joularEntityDTO);

        assertEquals(maybeJoularEntityListDTO, joularEntityListDTO);
    }*/

    /*@Test
    void complexGetClassMethodLineTest() {
        String line = "org.apache.commons.configuration2.builder.BasicConfigurationBuilder.lambda$getFilteredParameters$0";
        JSONObject maybeClassMethodLine = joularEntityService.getClassMethodLine(line);

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.apache.commons.configuration2.builder.BasicConfigurationBuilder");
        classMethodLine.put("methodName", "");
    }*/

    @Test
    void simpleGetMatchCkJoularTest() {
        String line = "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 67";
        CkAggregateLineDTO maybeCkAggregateLineDTO = joularResourceService.getMatchCkJoular(line);

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
        CkAggregateLineDTO maybeCkAggregateLineDTO = joularResourceService.getMatchCkJoular(line);

        CkAggregateLineDTO ckAggregateLineDTO = new CkAggregateLineDTO();
        ckAggregateLineDTO.setClassName("org.apache.commons.configuration2.tree.DefaultConfigurationKey");
        ckAggregateLineDTO.setMethodName("nextDelimiterPos/1");
        ckAggregateLineDTO.setFilePath("filePath2");
        ckAggregateLineDTO.setLine(new ArrayList<>(){{add(660);}});
        ckAggregateLineDTO.setLoc(new ArrayList<>(){{add(10);}});

        assertEquals(maybeCkAggregateLineDTO, ckAggregateLineDTO);
    }

    @Test
    void createJoularEntityDTOListTest() {
        String csvPath = "joular-csv-test/1-1326858-1701080339565";
        Path iterationFilePath = Paths.get(csvPath);

        CommitSimpleDTO commitSimpleDTO = new CommitSimpleDTO();
        commitSimpleDTO.setSha("59e5152722198526c6ffe5361de7d1a6a87275c7");
        RepositorySimpleDTO repositorySimpleDTO = new RepositorySimpleDTO();
        repositorySimpleDTO.setName("commons-configuration");
        repositorySimpleDTO.setOwner("apache");
        commitSimpleDTO.setRepository(repositorySimpleDTO);
        joularResourceService.setCommitSimpleDTO(commitSimpleDTO);

        FileListProvider fileListProvider = new TestingFileListProvider();
        joularResourceService.setFileListProvider(fileListProvider);

        IterationDTO iterationDTO = joularEntityService.createIterationDTOFromCsvFileName("1-1326858-1701080339565");
        joularResourceService.setIterationDTO(iterationDTO);

        JoularEntityListDTO maybeJoularEntityListDTO = joularEntityService.createJoularEntityDTOList(iterationFilePath);

        MeasurableElementDTO methodElementDTO = new MeasurableElementDTO();
        methodElementDTO.setAstElem("method");
        methodElementDTO.setFilePath("filePath");
        methodElementDTO.setClassName("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        methodElementDTO.setMethodName("getFileNames/2");
        methodElementDTO.setClassMethodSignature("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames");

        JoularEntityDTO joularEntityDTO = getJoularEntityDTO(methodElementDTO);

        JoularEntityListDTO joularEntityListDTO = new JoularEntityListDTO();
        joularEntityListDTO.add(joularEntityDTO);

        assertEquals(maybeJoularEntityListDTO, joularEntityListDTO);
    }

    @NotNull
    private JoularEntityDTO getJoularEntityDTO(MeasurableElementDTO methodElementDTO) {
        IterationDTO iterationDTO = new IterationDTO();
        iterationDTO.setIterationId(1);
        iterationDTO.setPid(1326858);
        iterationDTO.setStartTimestamp(1701080339565L);

        CommitSimpleDTO commitSimpleDTO = new CommitSimpleDTO();
        RepositorySimpleDTO repositorySimpleDTO = new RepositorySimpleDTO();
        repositorySimpleDTO.setOwner(repository.getOwner());
        repositorySimpleDTO.setName(repository.getName());
        commitSimpleDTO.setRepository(repositorySimpleDTO);
        commitSimpleDTO.setSha("59e5152722198526c6ffe5361de7d1a6a87275c7");


        JoularEntityDTO joularEntityDTO = new JoularEntityDTO();
        joularEntityDTO.setValue(107.43F);
        joularEntityDTO.setScope("app");
        joularEntityDTO.setMonitoringType("total");
        joularEntityDTO.setMethodElementDTO(methodElementDTO);
        joularEntityDTO.setIterationDTO(iterationDTO);
        joularEntityDTO.setCommitSimpleDTO(commitSimpleDTO);
        return joularEntityDTO;
    }

    @Test
    void addOrUpdateJoularEntityListDTOHasNotMethodElementTest() {
        // Arrange
        String line = "org.apache.commons.configuration2.tree.DefaultConfigurationKey$KeyIterator.nextDelimiterPos 662";
        String classMethodSignature = joularEntityService.getClassMethodSignature(line);
        CkAggregateLineDTO matchedCkJoular = joularResourceService.getMatchCkJoular(line);
        MeasurableElementDTO methodElementDTO = Util.getMeasurableElementForJoular(matchedCkJoular, classMethodSignature);

        CommitSimpleDTO commitSimpleDTO = new CommitSimpleDTO();
        commitSimpleDTO.setSha("1234");
        RepositorySimpleDTO repositorySimpleDTO = new RepositorySimpleDTO();
        repositorySimpleDTO.setName("commons-configuration");
        repositorySimpleDTO.setOwner("apache");
        commitSimpleDTO.setRepository(repositorySimpleDTO);
        joularResourceService.setCommitSimpleDTO(commitSimpleDTO);

        MethodElementSetDTO methodElementSetDTO = new MethodElementSetDTO();
        joularResourceService.setMethodElementSetDTO(methodElementSetDTO);

        FileListProvider fileListProvider = new TestingFileListProvider();
        joularResourceService.setFileListProvider(fileListProvider);

        Float value = 2.3391F;

        IterationDTO iterationDTO = joularEntityService.createIterationDTOFromCsvFileName("1-1326858-1701080339565");
        joularResourceService.setIterationDTO(iterationDTO);

        JoularEntityListDTO maybeJoularEntityListDTO = new JoularEntityListDTO();
        joularResourceService.setJoularEntityListDTO(maybeJoularEntityListDTO);

        // Act
        joularEntityService.addOrUpdateJoularEntityListDTO(methodElementDTO, value);

        JoularEntityDTO joularEntityDTO = new JoularEntityDTO();
        joularEntityDTO.setIterationDTO(iterationDTO);
        joularEntityDTO.setCommitSimpleDTO(commitSimpleDTO);
        joularEntityDTO.setScope("app");
        joularEntityDTO.setMonitoringType("total");
        joularEntityDTO.setValue(value);
        joularEntityDTO.setMethodElementDTO(methodElementDTO);

        JoularEntityListDTO joularEntityListDTO = new JoularEntityListDTO();
        joularEntityListDTO.add(joularEntityDTO);

        // Assert
        assertEquals(maybeJoularEntityListDTO, joularEntityListDTO);
    }

    @Test
    void addOrUpdateJoularEntityListDTOHasMethodElementTest() {
        // Arrange
        String line = "org.apache.commons.configuration2.tree.DefaultConfigurationKey$KeyIterator.nextDelimiterPos 662";
        String classMethodSignature = joularEntityService.getClassMethodSignature(line);
        CkAggregateLineDTO matchedCkJoular = joularResourceService.getMatchCkJoular(line);
        MeasurableElementDTO methodElementDTO = Util.getMeasurableElementForJoular(matchedCkJoular, classMethodSignature);

        CommitSimpleDTO commitSimpleDTO = new CommitSimpleDTO();
        commitSimpleDTO.setSha("1234");
        RepositorySimpleDTO repositorySimpleDTO = new RepositorySimpleDTO();
        repositorySimpleDTO.setName("commons-configuration");
        repositorySimpleDTO.setOwner("apache");
        commitSimpleDTO.setRepository(repositorySimpleDTO);
        joularResourceService.setCommitSimpleDTO(commitSimpleDTO);

        MethodElementSetDTO methodElementSetDTO = new MethodElementSetDTO();
        methodElementSetDTO.add(methodElementDTO);
        joularResourceService.setMethodElementSetDTO(methodElementSetDTO);

        Float firstValue = 2.3391F;
        Float addedValue = 10F;

        IterationDTO iterationDTO = joularEntityService.createIterationDTOFromCsvFileName("1-1326858-1701080339565");
        joularResourceService.setIterationDTO(iterationDTO);

        JoularEntityListDTO maybeJoularEntityListDTO = new JoularEntityListDTO();
        JoularEntityDTO maybeJoularEntityDTO = new JoularEntityDTO();
        maybeJoularEntityDTO.setIterationDTO(iterationDTO);
        maybeJoularEntityDTO.setCommitSimpleDTO(commitSimpleDTO);
        maybeJoularEntityDTO.setScope("app");
        maybeJoularEntityDTO.setMonitoringType("total");
        maybeJoularEntityDTO.setValue(firstValue);
        maybeJoularEntityDTO.setMethodElementDTO(methodElementDTO);
        maybeJoularEntityListDTO.add(maybeJoularEntityDTO);
        joularResourceService.setJoularEntityListDTO(maybeJoularEntityListDTO);

        // Act
        joularEntityService.addOrUpdateJoularEntityListDTO(methodElementDTO,addedValue);

        JoularEntityDTO joularEntityDTO = new JoularEntityDTO();
        joularEntityDTO.setIterationDTO(iterationDTO);
        joularEntityDTO.setCommitSimpleDTO(commitSimpleDTO);
        joularEntityDTO.setScope("app");
        joularEntityDTO.setMonitoringType("total");
        joularEntityDTO.setValue(firstValue + addedValue);
        joularEntityDTO.setMethodElementDTO(methodElementDTO);

        JoularEntityListDTO joularEntityListDTO = new JoularEntityListDTO();
        joularEntityListDTO.add(joularEntityDTO);

        // Assert
        assertEquals(maybeJoularEntityListDTO, joularEntityListDTO);
    }
}
