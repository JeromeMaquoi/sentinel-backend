package com.snail.sentinel.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.IntegrationTest;
import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.service.dto.ck.CkEntityDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositorySimpleDTO;
import com.snail.sentinel.backend.service.impl.CkServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class CkServiceImplIT {

    private static final String DEFAULT_NAME = "commons-lang";

    private static final String DEFAULT_OWNER = "apache";

    private static final String DEFAULT_METRIC_NAME = "totalMethodsQty";

    private static final Object DEFAULT_VALUE = 4;

    private static final String DEFAULT_TOOL_VERSION = "toolVersion1.4";

    private static final String DEFAULT_SHA = "sha123456789";

    private static final String DEFAULT_AST_ELEM = "class";

    private static final String DEFAULT_CLASS_NAME = "className";

    private static final String DEFAULT_FILE_PATH = "/file/path/to/files";

    @Autowired
    private CkServiceImpl ckService;

    private RepositorySimpleDTO repositorySimpleDTO;

    private CommitSimpleDTO commitSimpleDTO;

    private MeasurableElementDTO measurableElementDTO;

    @BeforeEach
    public void init() {
        repositorySimpleDTO = new RepositorySimpleDTO();
        repositorySimpleDTO.setOwner(DEFAULT_OWNER);
        repositorySimpleDTO.setName(DEFAULT_NAME);

        commitSimpleDTO = new CommitSimpleDTO();
        commitSimpleDTO.setSha(DEFAULT_SHA);
        commitSimpleDTO.setRepository(repositorySimpleDTO);

        measurableElementDTO.setAstElem(DEFAULT_AST_ELEM);
        measurableElementDTO.setFilePath(DEFAULT_FILE_PATH);
        measurableElementDTO.setClassName(DEFAULT_CLASS_NAME);


    }

    @Test
    void buildNewCkEntity() {
        CkEntityDTO ckEntityDTO = new CkEntityDTO();
        ckEntityDTO.setName(DEFAULT_METRIC_NAME);
        ckEntityDTO.setValue(DEFAULT_VALUE);
        ckEntityDTO.setToolVersion(DEFAULT_TOOL_VERSION);
        ckEntityDTO.setCommit(commitSimpleDTO);
        ckEntityDTO.setMeasurableElementDTO(measurableElementDTO);

        CkEntity maybeCkEntity = ckService.buildNewCkEntity(ckEntityDTO);

        CkEntity ckEntity = new CkEntity();
        ckEntity.setMeasurableElement(measurableElementDTO);
        ckEntity.setName(DEFAULT_METRIC_NAME);
        ckEntity.setToolVersion(DEFAULT_TOOL_VERSION);
        ckEntity.setCommit(commitSimpleDTO);
        ckEntity.setValue(DEFAULT_VALUE);

        assertThat(maybeCkEntity).isEqualTo(ckEntity);
    }
}
