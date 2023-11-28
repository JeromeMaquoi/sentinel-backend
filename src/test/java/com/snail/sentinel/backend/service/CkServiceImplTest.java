package com.snail.sentinel.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.service.dto.ck.CkEntityDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.ClassElementDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositoryCompleteDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositorySimpleDTO;
import com.snail.sentinel.backend.service.impl.CkServiceImpl;
import com.snail.sentinel.backend.service.mapper.CkEntityMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

class CkServiceImplTest {

    @Mock
    private CkEntityRepository ckEntityRepository;

    @Mock
    private CkEntityMapper ckEntityMapper;

    @InjectMocks
    private CkServiceImpl ckService;

    private static final String AST_ELEM = Util.AST_ELEM_CLASS;

    private static final String DATE = "2023-07-29T12:34:09Z";

    private static final String MESSAGE = "[LANG-1704] ImmutablePair and ImmutableTriple implementation don't match\n" +
        "final in Javadoc";

    private static final String SHA = "9d85b0a11e5dbadd5da20865c3dd3f8ef4668c7d";

    private static final List<String> PARENTS_SHA = List.of("shashashasahsah");

    private static final String CLASS_NAME = "className";

    private static final String FILE_PATH = "/file/path/to/files";

    private static final String CLASS_TYPE = "innerclass";


    private static final String REPO_NAME = "commons-lang";

    private static final String REPO_OWNER = "apache";

    private static final String REPO_URL = "url/commons/lang";


    private static final JSONObject line = new JSONObject();

    private static CommitCompleteDTO commitCompleteDTO;

    private static CommitSimpleDTO commitSimpleDTO;

    private ClassElementDTO classElementDTO;

    @BeforeEach
    public void init() {
        ckEntityRepository = Mockito.mock(CkEntityRepository.class);
        ckService = new CkServiceImpl(ckEntityRepository, ckEntityMapper);

        line.put("loc", "3");
        line.put("fanout", "0");
        line.put("comparisonsQty", "0");
        line.put("line", "649");
        line.put("file", FILE_PATH);
        line.put("class", CLASS_NAME);
        line.put("type", "innerclass");

        RepositoryCompleteDTO repositoryCompleteDTO = new RepositoryCompleteDTO();
        repositoryCompleteDTO.setUrl(REPO_URL);
        repositoryCompleteDTO.setName(REPO_NAME);
        repositoryCompleteDTO.setOwner(REPO_OWNER);

        RepositorySimpleDTO repositorySimpleDTO = new RepositorySimpleDTO();
        repositorySimpleDTO.setName(REPO_NAME);
        repositorySimpleDTO.setOwner(REPO_OWNER);

        commitCompleteDTO = new CommitCompleteDTO();
        commitCompleteDTO.setDate(DATE);
        commitCompleteDTO.setMessage(MESSAGE);
        commitCompleteDTO.setParentsSha(PARENTS_SHA);
        commitCompleteDTO.setSha(SHA);
        commitCompleteDTO.setRepository(repositoryCompleteDTO);

        commitSimpleDTO = new CommitSimpleDTO();
        commitSimpleDTO.setRepository(repositorySimpleDTO);
        commitSimpleDTO.setSha(SHA);

        classElementDTO = new ClassElementDTO();
        classElementDTO.setAstElem(AST_ELEM);
        classElementDTO.setFilePath(FILE_PATH);
        classElementDTO.setClassName(CLASS_NAME);
        classElementDTO.setClassType(CLASS_TYPE);
    }

    @Test
    void createCkEntityDTOForListTest() {
        CkEntityDTO maybeEntity = ckService.createCkEntityDTOForList(AST_ELEM, line, commitCompleteDTO, "loc");

        CkEntityDTO ckEntityDTO = new CkEntityDTO();
        ckEntityDTO.setCommit(commitSimpleDTO);
        ckEntityDTO.setToolVersion(Util.TOOL_VERSION);
        ckEntityDTO.setValue(3.0);
        ckEntityDTO.setName("loc");
        ckEntityDTO.setMeasurableElementDTO(classElementDTO);

        assertThat(maybeEntity).isEqualTo(ckEntityDTO);
    }

    @Test
    void getMeasurableElementTest() {
        MeasurableElementDTO maybeElement = Util.getMeasurableElement(AST_ELEM, line);
        ClassElementDTO measurableElementDTO = new ClassElementDTO();
        measurableElementDTO.setAstElem(AST_ELEM);
        measurableElementDTO.setFilePath(line.getString(Util.FILE));
        measurableElementDTO.setClassName(line.getString(Util.AST_ELEM_CLASS));
        measurableElementDTO.setClassType(line.getString("type"));

        assertThat(maybeElement).isEqualTo(measurableElementDTO);
    }
}
