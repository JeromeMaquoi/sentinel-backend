package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.commons.FileListProvider;
import com.snail.sentinel.backend.commons.TestingFileListProvider;
import com.snail.sentinel.backend.repository.JoularNodeEntityRepository;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularNodeEntityListDTO;
import com.snail.sentinel.backend.service.dto.joularnode.JoularNodeHashMapDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.impl.JoularNodeEntityServiceImpl;
import com.snail.sentinel.backend.service.impl.JoularResourceServiceImpl;
import com.snail.sentinel.backend.service.mapper.JoularNodeEntityMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class JoularNodeEntityServiceImplTest {
    @Mock
    private JoularResourceServiceImpl joularResourceService;
    @Mock
    private JoularNodeEntityMapper joularNodeEntityMapper;
    @Mock
    private JoularNodeEntityRepository joularNodeEntityRepository;
    @InjectMocks
    private JoularNodeEntityServiceImpl joularNodeEntityService;

    private CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO;

    private CkAggregateLineDTO ckAggregateLineDTO1;

    private CkAggregateLineDTO ckAggregateLineDTO2;

    private CkAggregateLineDTO ckAggregateLineDTO3;

    private String filePath = "filePath";

    @BeforeEach
    public void init() {
        joularResourceService = mock(JoularResourceServiceImpl.class);
        when(joularResourceService.getIterationDTO()).thenReturn(new IterationDTO());
        when(joularResourceService.getCommitSimpleDTO()).thenReturn(new CommitSimpleDTO());
        when(joularResourceService.getJoularNodeEntityListDTO()).thenReturn(new JoularNodeEntityListDTO());

        joularNodeEntityRepository = mock(JoularNodeEntityRepository.class);
        joularNodeEntityMapper = mock(JoularNodeEntityMapper.class);
        joularNodeEntityService = new JoularNodeEntityServiceImpl(joularNodeEntityRepository, joularNodeEntityMapper, joularResourceService);

        // Alone method (line 6 of filtered-call-trees-energy csv file)
        ckAggregateLineDTO1 = createCkAggregateLineDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", 70, 10);

        // Two nodes (line 10)
        ckAggregateLineDTO2 = createCkAggregateLineDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest", "formatExample/0", 25, 5);
        ckAggregateLineDTO3 = createCkAggregateLineDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter", "format/1[java.lang.String]", 105, 15);

        ckAggregateLineHashMapDTO = new CkAggregateLineHashMapDTO();
        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO1);
        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO2);
        ckAggregateLineHashMapDTO.insertOne(ckAggregateLineDTO3);

        joularResourceService.setCkAggregateLineHashMapDTO(ckAggregateLineHashMapDTO);
        joularNodeEntityService.setJoularNodeHashMapDTO(new JoularNodeHashMapDTO());
        joularNodeEntityService.setJoularNodeEntityDTO(new JoularNodeEntityDTO());
    }

    public CkAggregateLineDTO createCkAggregateLineDTO(String className, String methodName, int line, int loc) {
        CkAggregateLineDTO ckAggregateLineDTO = new CkAggregateLineDTO();
        ckAggregateLineDTO.setClassName(className);
        ckAggregateLineDTO.setFilePath(filePath);
        ckAggregateLineDTO.setMethodName(methodName);
        ckAggregateLineDTO.setLine(new ArrayList<>(){{add(line);}});
        ckAggregateLineDTO.setLoc(new ArrayList<>(){{add(loc);}});
        return ckAggregateLineDTO;
    }

    public MeasurableElementDTO createMeasurableElementDTO(String className, String methodName, String classMethodSignature) {
        MeasurableElementDTO measurableElementDTO = new MeasurableElementDTO();
        measurableElementDTO.setAstElem("method");
        measurableElementDTO.setFilePath(filePath);
        measurableElementDTO.setClassName(className);
        measurableElementDTO.setMethodName(methodName);
        measurableElementDTO.setClassMethodSignature(classMethodSignature);
        return measurableElementDTO;
    }

    public JoularNodeEntityDTO createJoularNodeEntityDTO(String id, Integer lineNumber, Float value, MeasurableElementDTO measurableElement, List<String> ancestors, String parent) {
        JoularNodeEntityDTO joularNodeEntityDTO = new JoularNodeEntityDTO();
        joularNodeEntityDTO.setId(id);
        joularNodeEntityDTO.setScope("app");
        joularNodeEntityDTO.setMonitoringType("calltrees");
        joularNodeEntityDTO.setLineNumber(lineNumber);
        joularNodeEntityDTO.setValue(value);
        joularNodeEntityDTO.setMeasurableElement(measurableElement);
        joularNodeEntityDTO.setCommit(new CommitSimpleDTO());
        joularNodeEntityDTO.setAncestors(ancestors);
        joularNodeEntityDTO.setParent(parent);
        joularNodeEntityDTO.setIteration(new IterationDTO());
        return joularNodeEntityDTO;
    }


    @Test
    void simpleCreateJoularNodeEntityMeasurableElementTest() {
        String classMethodLineString = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75";
        when(joularResourceService.getMatchCkJoular(classMethodLineString)).thenReturn(Optional.ofNullable(ckAggregateLineDTO1));
        Optional<MeasurableElementDTO> optionalMeasurableElementDTO = joularNodeEntityService.createJoularNodeEntityMeasurableElement(classMethodLineString);
        assertTrue(optionalMeasurableElementDTO.isPresent());
        MeasurableElementDTO maybeMeasurableElementDTO = optionalMeasurableElementDTO.get();

        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");

        assertEquals(maybeMeasurableElementDTO, measurableElementDTO);
    }

    @Test
    void emptyCreateJoularNodeEntityMeasurableElementTest() {
        String classMethodLineString = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75555";
        Optional<MeasurableElementDTO> optionalMeasurableElementDTO = joularNodeEntityService.createJoularNodeEntityMeasurableElement(classMethodLineString);
        assertTrue(optionalMeasurableElementDTO.isEmpty());
    }

    @Test
    void simpleCreateJoularNodeEntityDTOTest() {
        String classMethodLineString = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75";
        Float value = 1.2417F;

        when(joularResourceService.getAncestors()).thenReturn(new ArrayList<>());
        when(joularResourceService.getMatchCkJoular(classMethodLineString)).thenReturn(Optional.ofNullable(ckAggregateLineDTO1));
        joularNodeEntityService.setLastElement(true);

        joularNodeEntityService.handleOneMethodFromOneCsvLine(classMethodLineString, value);
        JoularNodeEntityDTO maybeJoularNodeEntityDTO = joularNodeEntityService.getJoularNodeEntityDTO();

        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");
        JoularNodeEntityDTO goodJoularNodeEntityDTO = createJoularNodeEntityDTO(maybeJoularNodeEntityDTO.getId(), 75, value, measurableElementDTO, new ArrayList<>(), null);

        assertEquals(goodJoularNodeEntityDTO, maybeJoularNodeEntityDTO);
    }

    /*@Test
    void emptyMeasurableElementHandleOneMethodFromOneCsvLineTest() {
        String classMethodLineString = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75 75 75";
        Float value = 1.2417F;

        when(joularResourceService.getIterationDTO()).thenReturn(new IterationDTO(1, 111, 111));
        when(joularResourceService.getCommitSimpleDTO()).thenReturn(commitSimpleDTO);
        when(commitSimpleDTO.getRepository()).thenReturn(repositorySimpleDTO);
        when(repositorySimpleDTO.getName()).thenReturn("jabref");

        joularNodeEntityService.handleOneMethodFromOneCsvLine(classMethodLineString, value);
        verify(log).error(anyString());
    }*/

    @Test
    void childHandleOneMethodFromOneCsvLineTest() {
        String parentId = "1234";
        String classMethodLineString = "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format 111";
        Float value = 0.4679F;

        List<String> ancestors = new ArrayList<>(){{add(parentId);}};
        when(joularResourceService.getAncestors()).thenReturn(new ArrayList<>(){{add(parentId);}});
        when(joularResourceService.getMatchCkJoular(classMethodLineString)).thenReturn(Optional.ofNullable(ckAggregateLineDTO3));
        joularNodeEntityService.setLastElement(true);

        joularNodeEntityService.handleOneMethodFromOneCsvLine(classMethodLineString, value);
        JoularNodeEntityDTO maybeJoularNodeEntityDTO = joularNodeEntityService.getJoularNodeEntityDTO();

        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter", "format/1[java.lang.String]", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format");
        JoularNodeEntityDTO joularNodeEntityDTO = createJoularNodeEntityDTO(maybeJoularNodeEntityDTO.getId(), 111, value, measurableElementDTO, ancestors, parentId);

        assertEquals(joularNodeEntityDTO, maybeJoularNodeEntityDTO);
    }

    /*@Test
    void joularNodeEntityDTOInMapHandleOneMethodFromOneCsvLineTest() {
        String classMethodLineString = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75";
        Float value = 1.2417F;

        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");
        JoularNodeEntityDTO joularNodeEntityDTO = handleOneMethodFromOneCsvLine("1234", 75, value, measurableElementDTO, new ArrayList<>(), null);
        JoularNodeHashMapDTO joularNodeHashMapDTO = new JoularNodeHashMapDTO();
        joularNodeHashMapDTO.insertOne(joularNodeEntityDTO);
        joularNodeEntityService.setJoularNodeHashMapDTO(joularNodeHashMapDTO);

        Optional<JoularNodeEntityDTO> optionalMaybeJoularNodeEntityDTO = joularNodeEntityService.handleOneMethodFromOneCsvLine(classMethodLineString, value);
        assertTrue(optionalMaybeJoularNodeEntityDTO.isPresent());
        JoularNodeEntityDTO maybeJoularNodeEntityDTO = optionalMaybeJoularNodeEntityDTO.get();

        assertEquals(joularNodeEntityDTO, maybeJoularNodeEntityDTO);
    }*/

    @Test
    void oneNodeHandleOneCsvLineTest() {
        String classMethodLineString = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75";
        JSONObject line = new JSONObject();
        line.put(classMethodLineString, "1.2417");
        when(joularResourceService.getAncestors()).thenCallRealMethod();
        doCallRealMethod().when(joularResourceService).setAncestors(new ArrayList<>());
        JoularNodeEntityListDTO joularNodeEntityListDTO = new JoularNodeEntityListDTO();
        when(joularResourceService.getJoularNodeEntityListDTO()).thenReturn(joularNodeEntityListDTO);
        when(joularResourceService.getMatchCkJoular(classMethodLineString)).thenReturn(Optional.ofNullable(ckAggregateLineDTO1));

        joularNodeEntityService.handleOneCsvLine(line);

        List<String> ancestors = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");
        String id = joularNodeEntityListDTO.get(0).getId();
        JoularNodeEntityDTO joularNodeEntityDTO = createJoularNodeEntityDTO(id, 75, 1.2417F, measurableElementDTO, ancestors, null);

        assertEquals(joularNodeEntityDTO, joularNodeEntityListDTO.get(0));
    }

    @Test
    void twoNodesHandleOneCsvLineTest() {
        String classMethodLineString1 = "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest.formatExample 28";
        when(joularResourceService.getMatchCkJoular(classMethodLineString1)).thenReturn(Optional.ofNullable(ckAggregateLineDTO2));
        String classMethodLineString2 = "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format 111";
        when(joularResourceService.getMatchCkJoular(classMethodLineString2)).thenReturn(Optional.ofNullable(ckAggregateLineDTO3));
        String lineString = classMethodLineString1 + ";" + classMethodLineString2;
        String value = "0.4679";
        JSONObject line = new JSONObject();
        line.put(lineString, value);
        when(joularResourceService.getAncestors()).thenCallRealMethod();
        doCallRealMethod().when(joularResourceService).setAncestors(new ArrayList<>());

        JoularNodeEntityListDTO joularNodeEntityListDTO = new JoularNodeEntityListDTO();
        when(joularResourceService.getJoularNodeEntityListDTO()).thenReturn(joularNodeEntityListDTO);

        joularNodeEntityService.handleOneCsvLine(line);

        List<String> actualAncestors1 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO1 = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest", "formatExample/0", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest.formatExample");
        String id1 = joularNodeEntityListDTO.get(0).getId();

        JoularNodeEntityDTO joularNodeEntityDTO1 = createJoularNodeEntityDTO(id1, 28, null, measurableElementDTO1, actualAncestors1, null);

        List<String> actualAncestors2 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO2 = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter", "format/1[java.lang.String]", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format");
        String id2 = joularNodeEntityListDTO.get(1).getId();
        actualAncestors2.add(id1);
        JoularNodeEntityDTO joularNodeEntityDTO2 = createJoularNodeEntityDTO(id2, 111, 0.4679F, measurableElementDTO2, actualAncestors2, id1);

        assertThat(joularNodeEntityListDTO.getList()).containsExactly(joularNodeEntityDTO1, joularNodeEntityDTO2);
    }

    @Test
    void createJoularNodeEntityDTOListTest() {
        String classMethodLineString1 = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75";
        when(joularResourceService.getMatchCkJoular(classMethodLineString1)).thenReturn(Optional.ofNullable(ckAggregateLineDTO1));
        String classMethodLineString2 = "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest.formatExample 28";
        when(joularResourceService.getMatchCkJoular(classMethodLineString2)).thenReturn(Optional.ofNullable(ckAggregateLineDTO2));
        String classMethodLineString3 = "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format 111";
        when(joularResourceService.getMatchCkJoular(classMethodLineString3)).thenReturn(Optional.ofNullable(ckAggregateLineDTO3));

        when(joularResourceService.getAncestors()).thenCallRealMethod();
        doCallRealMethod().when(joularResourceService).setAncestors(new ArrayList<>());

        FileListProvider fileListProvider = new TestingFileListProvider();
        when(joularResourceService.getFileListProvider()).thenReturn(fileListProvider);

        JoularNodeEntityListDTO joularNodeEntityListDTO = new JoularNodeEntityListDTO();
        when(joularResourceService.getJoularNodeEntityListDTO()).thenReturn(joularNodeEntityListDTO);
        doCallRealMethod().when(joularResourceService).setJoularNodeEntityListDTO(new JoularNodeEntityListDTO());

        String iterationFilePath = "joular-csv-test/1-1326858-1701080339565";
        joularNodeEntityService.createJoularNodeEntityDTOList(Path.of(iterationFilePath));


        List<String> actualAncestors1 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");
        String id1 = joularNodeEntityListDTO.get(0).getId();
        JoularNodeEntityDTO joularNodeEntityDTO1 = createJoularNodeEntityDTO(id1, 75, 1.2417F, measurableElementDTO, actualAncestors1, null);

        List<String> actualAncestors2 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO2 = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest", "formatExample/0", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest.formatExample");
        String id2 = joularNodeEntityListDTO.get(1).getId();
        JoularNodeEntityDTO joularNodeEntityDTO2 = createJoularNodeEntityDTO(id2, 28, null, measurableElementDTO2, actualAncestors2, null);

        List<String> actualAncestors3 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO3 = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter", "format/1[java.lang.String]", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format");
        String id3 = joularNodeEntityListDTO.get(2).getId();
        actualAncestors3.add(id2);
        JoularNodeEntityDTO joularNodeEntityDTO3 = createJoularNodeEntityDTO(id3, 111, 0.4679F, measurableElementDTO3, actualAncestors3, id2);

        assertThat(joularNodeEntityListDTO.getList()).containsExactly(joularNodeEntityDTO1, joularNodeEntityDTO2, joularNodeEntityDTO3);
    }

    @Test
    void exceptionCreateJoularNodeEntityDTOList() {
        String iterationFilePath = "joular-csv-test/no-csv-line-exception";
        assertThrows(NullPointerException.class, () -> {
            joularNodeEntityService.createJoularNodeEntityDTOList(Path.of(iterationFilePath));
        });
        assertThrows(NullPointerException.class, () -> {
            joularNodeEntityService.createJoularNodeEntityDTOList(null);
        });
    }
}
