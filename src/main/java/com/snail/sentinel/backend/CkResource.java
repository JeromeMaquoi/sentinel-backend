package com.snail.sentinel.backend;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityListDTO;
import com.snail.sentinel.backend.service.impl.CkServiceImpl;
import com.snail.sentinel.backend.service.CommitService;
import com.snail.sentinel.backend.service.impl.JoularServiceImpl;
import com.snail.sentinel.backend.service.dto.ck.CkEntityDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CkResource {
    private final Logger log = LoggerFactory.getLogger(CkResource.class);

    private final List<HashMap<String, String>> repoData;

    private final CkServiceImpl ckService;

    private final CommitService commitService;

    private final JoularServiceImpl joularServiceImpl;

    public CkResource(CkServiceImpl ckService, CommitService commitService, JoularServiceImpl joularServiceImpl) {
        this.ckService = ckService;
        this.commitService = commitService;
        this.joularServiceImpl = joularServiceImpl;
        this.repoData = new ArrayList<>();
    }

    public void insertAllData() throws Exception {
        //ckService.deleteAll();
        joularServiceImpl.deleteAll();
        setRepoData();
        List<CommitCompleteDTO> listCommits = new ArrayList<>();
        // For each repository
        for (HashMap<String, String> repoItem : repoData) {
            JSONObject commitData = commitService.getCommitData(repoItem.get(Util.OWNER), repoItem.get(Util.NAME), repoItem.get(Util.SHA));

            // Preparation of the Commit data to be inserted
            CommitCompleteDTO commitCompleteDTO = commitService.createCommitEntityDTO(repoItem, commitData);
            //listCommits.add(commitCompleteDTO);

            // Preparation and insertion of CK data
            //List<CkEntityDTO> ckEntityDTOList = ckService.createCkEntityDTOList(repoItem, commitCompleteDTO);
            //insertCkData(ckEntityDTOList);

            // Preparation of the Joular data to be inserted
            CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO = ckService.aggregate(repoItem.get(Util.NAME));
            JoularEntityListDTO joularEntityDTOList = joularServiceImpl.createJoularEntityDTOList(repoItem, ckAggregateLineHashMapDTO, commitCompleteDTO);
            insertJoularData(joularEntityDTOList);

            log.info("Ending for the repository: {}", repoItem);
        }
        //insertCommits(listCommits);

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
        commitService.bulkAdd(listCommits);
        log.info("Created information for the list of CommitEntity");
    }

    public void insertCkData(List<CkEntityDTO> listCks) {
        ckService.bulkAdd(listCks);
        log.info("List of CkEntity inserted to the database");
    }

    public void insertJoularData(JoularEntityListDTO joularEntityListDTO) {
        joularServiceImpl.bulkAdd(joularEntityListDTO.getList());
        log.info("List of JoularEntity inserted to the database");
    }
}
