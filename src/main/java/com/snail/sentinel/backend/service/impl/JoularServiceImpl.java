package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.service.*;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.RepoDataDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Service
public class JoularServiceImpl implements JoularService {
    private final Logger log = LoggerFactory.getLogger(JoularServiceImpl.class);

    private final JoularResourceService joularResourceService;

    private final JoularEntityService joularEntityService;

    private final JoularNodeEntityService joularNodeEntityService;

    public JoularServiceImpl(JoularResourceService joularResourceService, JoularEntityService joularEntityService, JoularNodeEntityService joularNodeEntityService) {
        this.joularResourceService = joularResourceService;
        this.joularEntityService = joularEntityService;
        this.joularNodeEntityService = joularNodeEntityService;
    }
    @Override
    public void insertBatchJoularData(RepoDataDTO repoItem, JSONObject commitData) {
        log.info("insertBatchJoularData");
        joularResourceService.setCommitSimpleDTO(repoItem, commitData);
        joularResourceService.setCkAggregateLineHashMapDTO(repoItem.getName());
        List<File> iterationPaths = Util.searchDirectories("joularjx-result", new File(System.getenv("REPO_DIRECTORY") + repoItem.getName()));
        for (File iterationFilePath : iterationPaths) {
            handleOneProject(iterationFilePath.getAbsolutePath());
        }
    }

    public void handleOneProject(String iterationPath) {
        log.info("Request to create JoularEntityDTO and JoularNodeEntityDTO for {}", iterationPath);
        if (joularResourceService.getCkAggregateLineHashMapDTO() == null) {
            throw new IllegalStateException("ckAggregateLineHashMapDTO must be set before using handleOneJoularIteration method");
        }
        Set<Path> fileList = joularResourceService.getFileListProvider().getFileList(iterationPath);
        for (Path iterationFilePath : fileList) {
            handleOneIterationOfOneProject(iterationFilePath);
        }
    }

    public void handleOneIterationOfOneProject(Path iterationFilePath) {
        String csvPathFileName = iterationFilePath.getFileName().toString();
        IterationDTO iterationDTO = createIterationDTOFromCsvFileName(csvPathFileName);
        joularResourceService.setIterationDTO(iterationDTO);

        Util.writeTimeToFileWarningIterationTitle(iterationDTO.getIterationId());

        joularEntityService.handleJoularEntityCreationForOneIteration(iterationFilePath);
        Util.writeTimeToFileForIteration("JoularEntity", iterationDTO.getIterationId(), joularEntityService.getNumberOfMethods(), joularEntityService.getNumberOfUnhandledMethods());

        Util.writeTimeToFileForWarningEmptyLine();

        joularNodeEntityService.handleJoularNodeEntityCreationForOneIteration(iterationFilePath);
        Util.writeTimeToFileForIteration("JoularNodeEntity", iterationDTO.getIterationId(), joularNodeEntityService.getNumberOfMethods(), joularNodeEntityService.getNumberOfUnhandledMethods());
    }

    public IterationDTO createIterationDTOFromCsvFileName(String fileName) {
        Integer iterationId = parseInt(fileName.split("-")[0]);
        Integer pid = parseInt(fileName.split("-")[1]);
        long startTimestamp = parseLong(fileName.split("-")[2]);
        return new IterationDTO(iterationId, pid, startTimestamp);
    }
}
