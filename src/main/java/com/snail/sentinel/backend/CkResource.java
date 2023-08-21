package com.snail.sentinel.backend;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
import com.snail.sentinel.backend.service.CkService;
import com.snail.sentinel.backend.service.CommitService;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitEntityDTO;
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
        List<CommitEntityDTO> listCommits = new ArrayList<>();
        List<CkEntityDTO> listCks = new ArrayList<>();
        // For each repository
        for (HashMap<String, String> repoItem : repoData) {
            JSONObject jsonData = commitService.getCommitData(repoItem.get("owner"), repoItem.get("name"), repoItem.get("sha"));
            // Commit creation and adding to the list to be inserted into the DB
            CommitEntityDTO commitEntityDTO = commitService.createDTO(repoItem, jsonData);
            listCommits.add(commitEntityDTO);

            // Preparation of the CK data to be inserted

            // Preparation of the Joular data to be inserted
        }
        insertCommits(listCommits);
    }

    public void setRepoData() {
        HashMap<String, String> commonsLang = new HashMap<>();
        commonsLang.put("owner", "apache");
        commonsLang.put("name", "commons-lang");
        commonsLang.put("sha", "9d85b0a11e5dbadd5da20865c3dd3f8ef4668c7d");

        HashMap<String, String> commonsConfiguration = new HashMap<>();
        commonsConfiguration.put("owner", "apache");
        commonsConfiguration.put("name", "commons-configuration");
        commonsConfiguration.put("sha", "c13339a580ba8d4d4c1a6eba743cba6b02a0abdf");

        this.repoData.add(commonsLang);
        this.repoData.add(commonsConfiguration);
    }

    public void insertCommits(List<CommitEntityDTO> listCommits) {
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
