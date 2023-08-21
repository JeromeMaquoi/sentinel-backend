package com.snail.sentinel.backend;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
import com.snail.sentinel.backend.service.CkService;
import com.snail.sentinel.backend.service.CommitService;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class CkResource {
    private final Logger log = LoggerFactory.getLogger(CkResource.class);

    private String toolVersion = "ck-0.7.0-jar-with-dependencies";
    private List<String> astElem = Arrays.asList("method", "class", "variable");
    private final List<HashMap<String, String>> repoData;

    private final CkService ckService;

    private final CommitService commitService;

    private final CkEntityRepository ckEntityRepository;

    private final CommitEntityRepository commitEntityRepository;

    public CkResource(CkService ckService, CommitService commitService, CkEntityRepository ckEntityRepository, CommitEntityRepository commitEntityRepository) {
        this.ckService = ckService;
        this.commitService = commitService;
        this.ckEntityRepository = ckEntityRepository;
        this.commitEntityRepository = commitEntityRepository;
        this.repoData = new ArrayList<>();
    }

    public void insertAllData() throws Exception {
        setRepoData();
        List<CommitCompleteDTO> listCommits = new ArrayList<>();
        List<CkEntityDTO> listCks = new ArrayList<>();
        // For each repository
        for (HashMap<String, String> repoItem : repoData) {
            JSONObject commitData = commitService.getCommitData(repoItem.get(Util.OWNER), repoItem.get(Util.NAME), repoItem.get(Util.SHA));

            // Preparation of the Commit data to be inserted
            CommitCompleteDTO commitCompleteDTO = commitService.createCommitEntityDTO(repoItem, commitData);
            listCommits.add(commitCompleteDTO);

            // Preparation of the CK data to be inserted
            CkEntityDTO ckEntityDTO = ckService.createCkEntityDTO(repoItem, commitCompleteDTO);
            //log.debug("ckEntityDTO = " + ckEntityDTO.toString());
            listCks.add(ckEntityDTO);

            // Preparation of the Joular data to be inserted
        }
        insertCommits(listCommits);
    }

    public void setRepoData() {
        HashMap<String, String> commonsLang = new HashMap<>();
        commonsLang.put(Util.OWNER, "apache");
        commonsLang.put(Util.NAME, "commons-lang");
        commonsLang.put(Util.SHA, "9d85b0a11e5dbadd5da20865c3dd3f8ef4668c7d");

        HashMap<String, String> commonsConfiguration = new HashMap<>();
        commonsConfiguration.put(Util.OWNER, "apache");
        commonsConfiguration.put(Util.NAME, "commons-configuration");
        commonsConfiguration.put(Util.SHA, "c13339a580ba8d4d4c1a6eba743cba6b02a0abdf");

        this.repoData.add(commonsLang);
        this.repoData.add(commonsConfiguration);
    }

    public void insertCommits(List<CommitCompleteDTO> listCommits) {
        commitService.deleteAll();
        List<CommitEntity> listCommitEntities = commitService.bulkAdd(listCommits);
        log.info("List commit entities = " + listCommitEntities.toString());
    }

    public void insertCkData(List<CkEntityDTO> listCks) {
        ckService.deleteAll();
        List<CkEntity> listCkEntities = ckService.bulkAdd(listCks);
        log.info("List Ck entities = " + listCkEntities.toString());

    }
}
