package com.snail.sentinel.backend;

import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
import com.snail.sentinel.backend.service.CkEntityService;
import com.snail.sentinel.backend.service.CommitService;
import com.snail.sentinel.backend.service.dto.CommitEntityDTO;
import com.snail.sentinel.backend.service.dto.RepositoryDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CkResource {

    private String toolVersion = "ck-0.7.0-jar-with-dependencies";
    private List<String> astElem = Arrays.asList("method", "class", "variable");
    private List<HashMap<String, String>> repoData;

    private final CkEntityService ckEntityService;

    private final CommitService commitService;

    private final CkEntityRepository ckEntityRepository;

    private final CommitEntityRepository commitEntityRepository;

    public CkResource(CkEntityService ckEntityService, CommitService commitService, CkEntityRepository ckEntityRepository, CommitEntityRepository commitEntityRepository) {
        this.ckEntityService = ckEntityService;
        this.commitService = commitService;
        this.ckEntityRepository = ckEntityRepository;
        this.commitEntityRepository = commitEntityRepository;
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

    public CommitEntity createCommit(String owner, String repoName, String sha) {
        RepositoryDTO repository = new RepositoryDTO();
        repository.setName(repoName);
        repository.setOwner(owner);

        CommitEntityDTO commit = new CommitEntityDTO();
        commit.setSha(sha);
        commit.setRepositoryDTO(repository);

        return commitService.add(commit);
    }
}
