package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.commons.FileListProvider;
import com.snail.sentinel.backend.commons.TestingFileListProvider;
import com.snail.sentinel.backend.commons.TestingLoggingToFile;
import com.snail.sentinel.backend.repository.JoularNodeEntityRepository;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularNodeEntityListDTO;
import com.snail.sentinel.backend.service.dto.joularnode.JoularNodeHashMapDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.exceptions.CreateJoularNodeEntityMeasurableElementException;
import com.snail.sentinel.backend.service.impl.JoularNodeEntityServiceImpl;
import com.snail.sentinel.backend.service.impl.JoularResourceServiceImpl;
import com.snail.sentinel.backend.service.mapper.JoularNodeEntityMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class JoularNodeEntityServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(JoularNodeEntityServiceImplTest.class);
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

    private CkAggregateLineDTO ckAggregateLineDTO4;

    private CkAggregateLineDTO ckAggregateLineDTO5;

    private CkAggregateLineDTO ckAggregateLineDTO6;

    private final String filePath = "filePath";

    @BeforeEach
    public void init() {
        joularResourceService = mock(JoularResourceServiceImpl.class);
        when(joularResourceService.getIterationDTO()).thenReturn(new IterationDTO());
        when(joularResourceService.getCommitSimpleDTO()).thenReturn(new CommitSimpleDTO());
        when(joularResourceService.getJoularNodeEntityListDTO()).thenReturn(new JoularNodeEntityListDTO());
        when(joularResourceService.getLoggingToFile()).thenReturn(new TestingLoggingToFile());

        joularNodeEntityRepository = mock(JoularNodeEntityRepository.class);
        joularNodeEntityMapper = mock(JoularNodeEntityMapper.class);
        joularNodeEntityService = new JoularNodeEntityServiceImpl(joularNodeEntityRepository, joularNodeEntityMapper, joularResourceService);

        // Alone method (line 6 of filtered-call-trees-energy csv file)
        ckAggregateLineDTO1 = createCkAggregateLineDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", 70, 10);

        // Two nodes (line 10)
        ckAggregateLineDTO2 = createCkAggregateLineDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest", "formatExample/0", 25, 5);
        ckAggregateLineDTO3 = createCkAggregateLineDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter", "format/1[java.lang.String]", 105, 15);

        ckAggregateLineDTO4 = createCkAggregateLineDTO("org.jabref.logic.crawler.StudyYamlParser", "parseStudyYamlFile/0", 20, 10);

        ckAggregateLineDTO5 = createCkAggregateLineDTO("org.jabref.logic.crawler.StudyRepositoryTest", "getTestStudyRepository/0", 180, 10);

        ckAggregateLineDTO6 = createCkAggregateLineDTO("org.jabref.logic.bibtex.comparator.FieldComparator", "compare/0", 95, 5);

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
        MeasurableElementDTO maybeMeasurableElementDTO = optionalMeasurableElementDTO.orElseThrow(() -> new CreateJoularNodeEntityMeasurableElementException(classMethodLineString));

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
    void simpleHandleOneMethodFromOneCsvLineTest() {
        String classMethodLineString = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75";
        Float value = 1.2417F;

        when(joularResourceService.getAncestors()).thenReturn(new ArrayList<>());
        when(joularResourceService.getMatchCkJoular(classMethodLineString)).thenReturn(Optional.ofNullable(ckAggregateLineDTO1));
        joularNodeEntityService.setLastElement(true);
        joularNodeEntityService.setMethodsOfCurrentLine();

        joularNodeEntityService.handleOneMethodFromOneCsvLine(classMethodLineString, value);
        JoularNodeEntityDTO maybeJoularNodeEntityDTO = joularNodeEntityService.getJoularNodeEntityDTO();

        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");
        JoularNodeEntityDTO goodJoularNodeEntityDTO = createJoularNodeEntityDTO(maybeJoularNodeEntityDTO.getId(), 75, value, measurableElementDTO, new ArrayList<>(), null);

        List<JoularNodeEntityDTO> methodsOfCurrentLine = new ArrayList<>();
        methodsOfCurrentLine.add(goodJoularNodeEntityDTO);
        assertEquals(methodsOfCurrentLine, joularNodeEntityService.getMethodsOfCurrentLine());

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
        joularNodeEntityService.setMethodsOfCurrentLine();

        joularNodeEntityService.handleOneMethodFromOneCsvLine(classMethodLineString, value);
        JoularNodeEntityDTO maybeJoularNodeEntityDTO = joularNodeEntityService.getJoularNodeEntityDTO();

        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter", "format/1[java.lang.String]", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format");
        JoularNodeEntityDTO joularNodeEntityDTO = createJoularNodeEntityDTO(maybeJoularNodeEntityDTO.getId(), 111, value, measurableElementDTO, ancestors, parentId);

        List<JoularNodeEntityDTO> methodsOfCurrentLine = new ArrayList<>();
        methodsOfCurrentLine.add(joularNodeEntityDTO);
        assertEquals(methodsOfCurrentLine, joularNodeEntityService.getMethodsOfCurrentLine());

        assertEquals(joularNodeEntityDTO, maybeJoularNodeEntityDTO);
    }

    @Test
    void emptyHandleOneMethodFromOneCsvLineTest() {
        String classMethodLineString = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75555";
        Float value = 1.2417F;

        joularNodeEntityService.setMethodsOfCurrentLine();

        joularNodeEntityService.handleOneMethodFromOneCsvLine(classMethodLineString, value);
        assertTrue(joularNodeEntityService.getIgnoreLine());
    }

    @Test
    void exceptionHandleOneMethodFromOneCsvLineTest() {
        String classMethodLineString = "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format 111";
        Float value = 0.4679F;
        when(joularResourceService.getJoularNodeEntityListDTO()).thenReturn(null);
        joularNodeEntityService.setJoularNodeHashMapDTO(null);
        assertThrows(AssertionError.class, () -> {
            joularNodeEntityService.handleOneMethodFromOneCsvLine(classMethodLineString, value);
        });
        joularNodeEntityService.setJoularNodeHashMapDTO(new JoularNodeHashMapDTO());
        assertThrows(AssertionError.class, () -> {
            joularNodeEntityService.handleOneMethodFromOneCsvLine(classMethodLineString, value);
        });
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

        joularNodeEntityService.handleOneCsvLine(1, line);

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

        joularNodeEntityService.handleOneCsvLine(1, line);

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
    void duplicatesCreateJoularNodeEntityDTOListTest() {
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

        JoularNodeEntityListDTO maybeJoularNodeEntityListDTO = new JoularNodeEntityListDTO();
        when(joularResourceService.getJoularNodeEntityListDTO()).thenReturn(maybeJoularNodeEntityListDTO);
        doCallRealMethod().when(joularResourceService).setJoularNodeEntityListDTO(new JoularNodeEntityListDTO());

        String iterationFilePath = "joular-csv-test/2-80461-1708609634917";
        joularNodeEntityService.createJoularNodeEntityDTOList(Path.of(iterationFilePath));


        List<String> actualAncestors1 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");
        String id1 = maybeJoularNodeEntityListDTO.get(0).getId();
        JoularNodeEntityDTO joularNodeEntityDTO1 = createJoularNodeEntityDTO(id1, 75, 1.2417F, measurableElementDTO, actualAncestors1, null);

        List<String> actualAncestors2 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO2 = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest", "formatExample/0", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest.formatExample");
        String id2 = maybeJoularNodeEntityListDTO.get(1).getId();
        actualAncestors2.add(id1);
        JoularNodeEntityDTO joularNodeEntityDTO2 = createJoularNodeEntityDTO(id2, 28, null, measurableElementDTO2, actualAncestors2, id1);

        List<String> actualAncestors3 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO3 = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter", "format/1[java.lang.String]", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format");
        String id3 = maybeJoularNodeEntityListDTO.get(2).getId();
        actualAncestors3.add(id1);
        actualAncestors3.add(id2);
        JoularNodeEntityDTO joularNodeEntityDTO3 = createJoularNodeEntityDTO(id3, 111, 0.4679F, measurableElementDTO3, actualAncestors3, id2);

        assertThat(maybeJoularNodeEntityListDTO.getList()).containsExactlyInAnyOrder(joularNodeEntityDTO1, joularNodeEntityDTO2, joularNodeEntityDTO3);
    }

    @Test
    void sameMethodWithDifferentValueCreateJoularNodeEntityDTOListTest() {
        String classMethodLineString1 = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75";
        when(joularResourceService.getMatchCkJoular(classMethodLineString1)).thenReturn(Optional.ofNullable(ckAggregateLineDTO1));

        String classMethodLineString4 = "org.jabref.logic.crawler.StudyYamlParser.parseStudyYamlFile 25";
        when(joularResourceService.getMatchCkJoular(classMethodLineString4)).thenReturn(Optional.ofNullable(ckAggregateLineDTO4));

        String classMethodLineString2 = "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest.formatExample 28";
        when(joularResourceService.getMatchCkJoular(classMethodLineString2)).thenReturn(Optional.ofNullable(ckAggregateLineDTO2));


        when(joularResourceService.getAncestors()).thenCallRealMethod();
        doCallRealMethod().when(joularResourceService).setAncestors(new ArrayList<>());

        FileListProvider fileListProvider = new TestingFileListProvider();
        when(joularResourceService.getFileListProvider()).thenReturn(fileListProvider);

        JoularNodeEntityListDTO maybeJoularNodeEntityListDTO = new JoularNodeEntityListDTO();
        when(joularResourceService.getJoularNodeEntityListDTO()).thenReturn(maybeJoularNodeEntityListDTO);
        doCallRealMethod().when(joularResourceService).setJoularNodeEntityListDTO(new JoularNodeEntityListDTO());


        String iterationFilePath = "joular-csv-test/3-82212-1708609756976";
        joularNodeEntityService.createJoularNodeEntityDTOList(Path.of(iterationFilePath));


        List<String> actualAncestors1 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");
        String id1 = maybeJoularNodeEntityListDTO.get(0).getId();
        JoularNodeEntityDTO joularNodeEntityDTO1 = createJoularNodeEntityDTO(id1, 75, null, measurableElementDTO, actualAncestors1, null);

        List<String> actualAncestors4 = new ArrayList<>();
        actualAncestors4.add(id1);
        MeasurableElementDTO measurableElementDTO4 = createMeasurableElementDTO("org.jabref.logic.crawler.StudyYamlParser", "parseStudyYamlFile/0", "org.jabref.logic.crawler.StudyYamlParser.parseStudyYamlFile");
        String id4 = maybeJoularNodeEntityListDTO.get(1).getId();
        JoularNodeEntityDTO joularNodeEntityDTO4 = createJoularNodeEntityDTO(id4, 25, 1.2417F, measurableElementDTO4, actualAncestors4, id1);

        List<String> actualAncestors2 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO2 = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest", "formatExample/0", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest.formatExample");
        String id2 = maybeJoularNodeEntityListDTO.get(2).getId();
        actualAncestors2.add(id1);
        JoularNodeEntityDTO joularNodeEntityDTO2 = createJoularNodeEntityDTO(id2, 28, null, measurableElementDTO2, actualAncestors2, id1);

        List<String> actualAncestors5 = new ArrayList<>();
        actualAncestors5.add(id1);
        actualAncestors5.add(id2);
        String id5 = maybeJoularNodeEntityListDTO.get(3).getId();
        JoularNodeEntityDTO joularNodeEntityDTO5 = createJoularNodeEntityDTO(id5, 25, 0.4679F, measurableElementDTO4, actualAncestors5, id2);

        assertThat(maybeJoularNodeEntityListDTO.getList()).containsExactlyInAnyOrder(joularNodeEntityDTO1, joularNodeEntityDTO4, joularNodeEntityDTO2, joularNodeEntityDTO5);
    }

    @Test
    void sameMethodWithDifferentAncestorsCreateJoularNodeEntityDTOListTest() {
        String classMethodLineString1 = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75";
        when(joularResourceService.getMatchCkJoular(classMethodLineString1)).thenReturn(Optional.ofNullable(ckAggregateLineDTO1));

        String classMethodLineString2 = "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest.formatExample 28";
        when(joularResourceService.getMatchCkJoular(classMethodLineString2)).thenReturn(Optional.ofNullable(ckAggregateLineDTO2));

        String classMethodLineString4 = "org.jabref.logic.crawler.StudyYamlParser.parseStudyYamlFile 25";
        when(joularResourceService.getMatchCkJoular(classMethodLineString4)).thenReturn(Optional.ofNullable(ckAggregateLineDTO4));

        String classMethodLineString5 = "org.jabref.logic.crawler.StudyRepositoryTest.getTestStudyRepository 188";
        when(joularResourceService.getMatchCkJoular(classMethodLineString5)).thenReturn(Optional.ofNullable(ckAggregateLineDTO5));

        String classMethodLineString6 = "org.jabref.logic.bibtex.comparator.FieldComparator.compare 97";
        when(joularResourceService.getMatchCkJoular(classMethodLineString6)).thenReturn(Optional.ofNullable(ckAggregateLineDTO6));


        when(joularResourceService.getAncestors()).thenCallRealMethod();
        doCallRealMethod().when(joularResourceService).setAncestors(new ArrayList<>());
        FileListProvider fileListProvider = new TestingFileListProvider();
        when(joularResourceService.getFileListProvider()).thenReturn(fileListProvider);

        JoularNodeEntityListDTO maybeJoularNodeEntityListDTO = new JoularNodeEntityListDTO();
        when(joularResourceService.getJoularNodeEntityListDTO()).thenReturn(maybeJoularNodeEntityListDTO);
        doCallRealMethod().when(joularResourceService).setJoularNodeEntityListDTO(new JoularNodeEntityListDTO());


        String iterationFilePath = "joular-csv-test/4-83931-1708609873722";
        joularNodeEntityService.createJoularNodeEntityDTOList(Path.of(iterationFilePath));


        List<String> actualAncestors1 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");
        String id1 = maybeJoularNodeEntityListDTO.get(0).getId();
        JoularNodeEntityDTO joularNodeEntityDTO1 = createJoularNodeEntityDTO(id1, 75, null, measurableElementDTO, actualAncestors1, null);

        List<String> actualAncestors2 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO2 = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest", "formatExample/0", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatterTest.formatExample");
        String id2 = maybeJoularNodeEntityListDTO.get(1).getId();
        actualAncestors2.add(id1);
        JoularNodeEntityDTO joularNodeEntityDTO2 = createJoularNodeEntityDTO(id2, 28, null, measurableElementDTO2, actualAncestors2, id1);

        List<String> actualAncestors4 = new ArrayList<>();
        actualAncestors4.add(id1);
        actualAncestors4.add(id2);
        MeasurableElementDTO measurableElementDTO4 = createMeasurableElementDTO("org.jabref.logic.crawler.StudyYamlParser", "parseStudyYamlFile/0", "org.jabref.logic.crawler.StudyYamlParser.parseStudyYamlFile");
        String id4 = maybeJoularNodeEntityListDTO.get(2).getId();
        JoularNodeEntityDTO joularNodeEntityDTO4 = createJoularNodeEntityDTO(id4, 25, 1.2417F, measurableElementDTO4, actualAncestors4, id2);

        List<String> actualAncestors5 = new ArrayList<>();
        MeasurableElementDTO measurableElementDTO5 = createMeasurableElementDTO("org.jabref.logic.crawler.StudyRepositoryTest", "getTestStudyRepository/0", "org.jabref.logic.crawler.StudyRepositoryTest.getTestStudyRepository");
        String id5 = maybeJoularNodeEntityListDTO.get(3).getId();
        JoularNodeEntityDTO joularNodeEntityDTO5 = createJoularNodeEntityDTO(id5, 188, null, measurableElementDTO5, actualAncestors5, null);

        List<String> actualAncestors3 = new ArrayList<>();
        actualAncestors3.add(id5);
        String id3 = maybeJoularNodeEntityListDTO.get(4).getId();
        JoularNodeEntityDTO joularNodeEntityDTO3 = createJoularNodeEntityDTO(id3, 28, null, measurableElementDTO2, actualAncestors3, id5);

        List<String> actualAncestors6 = new ArrayList<>();
        actualAncestors6.add(id5);
        actualAncestors6.add(id3);
        MeasurableElementDTO measurableElementDTO6 = createMeasurableElementDTO("org.jabref.logic.bibtex.comparator.FieldComparator", "compare/0", "org.jabref.logic.bibtex.comparator.FieldComparator.compare");
        String id6 = maybeJoularNodeEntityListDTO.get(5).getId();
        JoularNodeEntityDTO joularNodeEntityDTO6 = createJoularNodeEntityDTO(id6, 97, 0.4679F, measurableElementDTO6, actualAncestors6, id3);

        assertThat(maybeJoularNodeEntityListDTO.getList()).containsExactlyInAnyOrder(joularNodeEntityDTO1, joularNodeEntityDTO2, joularNodeEntityDTO4, joularNodeEntityDTO5, joularNodeEntityDTO3, joularNodeEntityDTO6);
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
