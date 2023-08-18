package com.snail.sentinel.backend;

import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
import com.snail.sentinel.backend.service.CkService;
import com.snail.sentinel.backend.service.CommitService;
import com.snail.sentinel.backend.service.dto.CommitEntityDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class CkResource {

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

    public void insertAllData() {
        setRepoData();
        insertCommits();
        insertCkData();
    }

    public void setRepoData() {
        HashMap<String, String> commonsLang = new HashMap<>();
        commonsLang.put("owner", "apache");
        commonsLang.put("repo", "commons-lang");
        commonsLang.put("sha", "9d85b0a11e5dbadd5da20865c3dd3f8ef4668c7d");

        HashMap<String, String> commonsConfiguration = new HashMap<>();
        commonsConfiguration.put("owner", "apache");
        commonsConfiguration.put("repo", "commons-configuration");
        commonsConfiguration.put("sha", "c13339a580ba8d4d4c1a6eba743cba6b02a0abdf");

        this.repoData.add(commonsLang);
        this.repoData.add(commonsConfiguration);
    }

    public void insertCkData() {
        ckService.deleteAll();

    }

    public void insertCommits(){
        commitService.deleteAll();
        commitService.bulkAdd(createCommitList());
    }

    public List<CommitEntityDTO> createCommitList() {
        List<CommitEntityDTO> newList = new ArrayList<>();
        for (HashMap<String, String> item : repoData) {
            CommitEntityDTO commitEntityDTO = new CommitEntityDTO();
            commitEntityDTO.setOwner(item.get("owner"));
            commitEntityDTO.setRepoName(item.get("repo"));
            commitEntityDTO.setSha(item.get("sha"));
            newList.add(commitEntityDTO);
        }
        return newList;
    }
}
