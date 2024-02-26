package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.FileListProvider;
import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.repository.CkEntityRepositoryAggregation;
import com.snail.sentinel.backend.service.CommitEntityService;
import com.snail.sentinel.backend.service.JoularEntityService;
import com.snail.sentinel.backend.service.JoularService;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Service
public class JoularServiceImpl implements JoularService {
    private final Logger log = LoggerFactory.getLogger(JoularServiceImpl.class);

    private final CkEntityRepositoryAggregation ckEntityRepositoryAggregation;

    private final FileListProvider fileListProvider;

    private final JoularEntityService joularEntityService;

    private final CommitEntityService commitEntityService;

    private CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO;

    private CommitSimpleDTO commitSimpleDTO;

    public JoularServiceImpl(CkEntityRepositoryAggregation ckEntityRepositoryAggregation, FileListProvider fileListProvider, JoularEntityService joularEntityService, CommitEntityService commitEntityService) {
        this.ckEntityRepositoryAggregation = ckEntityRepositoryAggregation;
        this.fileListProvider = fileListProvider;
        this.joularEntityService = joularEntityService;
        this.commitEntityService = commitEntityService;
    }
    @Override
    public void insertBatchJoularData(HashMap<String, String> repoItem) {
        setCkAggregateLineHashMapDTO(repoItem.get(Util.NAME));
        List<File> iterationPaths = Util.searchDirectories("joularjx-result", new File(System.getenv("REPO_DIRECTORY") + repoItem.get(Util.NAME)));
        for (File iterationFilePath : iterationPaths) {
            handleOneProject(iterationFilePath.getAbsolutePath());
        }
    }

    @Override
    public void setCommitSimpleDTO(HashMap<String, String> repoItem, JSONObject commitData) {
        CommitCompleteDTO commitCompleteDTO = commitEntityService.createCommitEntityDTO(repoItem, commitData);
        this.commitSimpleDTO = Util.createCommitSimpleFromCommitCompleteDTO(commitCompleteDTO);
    }

    public void setCkAggregateLineHashMapDTO(String repoName) {
        this.ckAggregateLineHashMapDTO = ckEntityRepositoryAggregation.aggregate(repoName);
    }

    public void handleOneProject(String iterationPath) {
        log.info("Request to create JoularEntityDTO and JoularNodeEntityDTO for {}", iterationPath);
        if (ckAggregateLineHashMapDTO == null) {
            throw new IllegalStateException("ckAggregateLineHashMapDTO must be set before using handleOneJoularIteration method");
        }
        Set<Path> fileList = fileListProvider.getFileList(iterationPath);
        for (Path iterationFilePath : fileList) {
            handleOneIterationOfOneProject(iterationFilePath);
        }
    }

    public void handleOneIterationOfOneProject(Path iterationFilePath) {
        String csvPathFileName = iterationFilePath.getFileName().toString();
        IterationDTO iterationDTO = createIterationDTOFromCsvFileName(csvPathFileName);
        joularEntityService.handleJoularEntityCreationForOneIteration(iterationFilePath, commitSimpleDTO, iterationDTO, fileListProvider, ckAggregateLineHashMapDTO);
        // TODO call joularNodeEntityService to handle one iteration data
    }

    public IterationDTO createIterationDTOFromCsvFileName(String fileName) {
        Integer iterationId = parseInt(fileName.split("-")[0]);
        Integer pid = parseInt(fileName.split("-")[1]);
        long startTimestamp = parseLong(fileName.split("-")[2]);
        return new IterationDTO(iterationId, pid, startTimestamp);
    }
}
