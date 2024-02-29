package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.repository.JoularNodeEntityRepository;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.impl.JoularNodeEntityServiceImpl;
import com.snail.sentinel.backend.service.impl.JoularResourceServiceImpl;
import com.snail.sentinel.backend.service.mapper.JoularNodeEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JoularNodeEntityServiceImplTest {
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
        when(joularResourceService.getMatchCkJoular("org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75")).thenReturn(Optional.ofNullable(ckAggregateLineDTO1));
        String classMethodLineString = "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp 75";
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

        JoularNodeEntityDTO maybeJoularNodeEntityDTO = joularNodeEntityService.createJoularNodeEntityDTO(classMethodLineString, value);

        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.gui.fieldeditors.LinkedFileViewModelTest", "setUp/1[java.nio.file.Path]", "org.jabref.gui.fieldeditors.LinkedFileViewModelTest.setUp");
        JoularNodeEntityDTO joularNodeEntityDTO = createJoularNodeEntityDTO(maybeJoularNodeEntityDTO.getId(), 75, value, measurableElementDTO, new ArrayList<>(), null);

        assertEquals(joularNodeEntityDTO, maybeJoularNodeEntityDTO);
    }

    @Test
    void childCreateJoularNodeEntityDTOTest() {
        String parentId = "1234";
        String classMethodLineString = "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format 111";
        Float value = 0.4679F;

        List<String> ancestors = new ArrayList<>(){{add(parentId);}};
        when(joularResourceService.getAncestors()).thenReturn(ancestors);
        when(joularResourceService.getMatchCkJoular(classMethodLineString)).thenReturn(Optional.ofNullable(ckAggregateLineDTO3));
        joularNodeEntityService.setLastElement(true);

        JoularNodeEntityDTO maybeJoularNodeEntityDTO = joularNodeEntityService.createJoularNodeEntityDTO(classMethodLineString, value);

        MeasurableElementDTO measurableElementDTO = createMeasurableElementDTO("org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter", "format/1[java.lang.String]", "org.jabref.logic.formatter.bibtexfields.UnitsToLatexFormatter.format");
        JoularNodeEntityDTO joularNodeEntityDTO = createJoularNodeEntityDTO(maybeJoularNodeEntityDTO.getId(), 111, value, measurableElementDTO, ancestors, parentId);

        assertEquals(joularNodeEntityDTO, maybeJoularNodeEntityDTO);
    }
}
