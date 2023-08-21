package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CkService {
    private final Logger log = LoggerFactory.getLogger(CkService.class);

    private final CkEntityRepository ckEntityRepository;

    public CkService(CkEntityRepository ckEntityRepository) {
        this.ckEntityRepository = ckEntityRepository;
    }

    public List<CkEntity> findAll() {
        return ckEntityRepository.findAll();
    }

    public List<CkEntity> bulkAdd(List<CkEntityDTO> listCk) {
        return ckEntityRepository.insert(listCk.stream().map(this::buildNewCkEntity).toList());
    }

    public void deleteAll() {
        ckEntityRepository.deleteAll();
    }

    public CkEntity buildNewCkEntity(CkEntityDTO ckEntityDTO) {
        CkEntity newCk = new CkEntity();
        newCk.setName(ckEntityDTO.getName());
        newCk.setValue(ckEntityDTO.getValue());
        newCk.setToolVersion(ckEntityDTO.getToolVersion());
        newCk.setCommit(ckEntityDTO.getCommit());
        newCk.setMeasurableElement(ckEntityDTO.getMeasurableElementDTO());
        log.debug("Created Information for Ck: {}", newCk);
        return newCk;
    }

    public CkEntityDTO createCkEntityDTO(Map<String, String> repoItem, CommitCompleteDTO commitCompleteDTO) {
        CkEntityDTO ckEntityDTO = new CkEntityDTO();
        ckEntityDTO.setCommit(createCommitSimpleFromCommitCompleteDTO(commitCompleteDTO));

        String csvPath = System.getenv("REPO_PATH") + repoItem.get(Util.NAME) + "/joularjx-result/";
        List<File> listDir = listDir(new File(csvPath));

        /*for (File file: listDir) {

        }*/

        return null;
    }

    private CommitSimpleDTO createCommitSimpleFromCommitCompleteDTO(CommitCompleteDTO commitCompleteDTO) {
        CommitSimpleDTO commitSimpleDTO = new CommitSimpleDTO();
        commitSimpleDTO.setSha(commitCompleteDTO.getSha());
        commitSimpleDTO.setRepositorySimpleDTO(commitSimpleDTO.getRepositorySimpleDTO());
        return commitSimpleDTO;
    }

    private static List<File> listDir(File currentDirectory) {
        File[] filesList = currentDirectory.listFiles();
        assert filesList != null;
        return new ArrayList<>(Arrays.asList(filesList));
    }
}
