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

import java.io.File;
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

    private long startTime;

    public CkResource(CkServiceImpl ckService, CommitService commitService, JoularServiceImpl joularServiceImpl) {
        this.ckService = ckService;
        this.commitService = commitService;
        this.joularServiceImpl = joularServiceImpl;
        this.repoData = new ArrayList<>();
    }

    public void insertAllData() throws Exception {
        startTime = System.currentTimeMillis();

        ckService.deleteAll();
        joularServiceImpl.deleteAll();
        setRepoData();
        List<CommitCompleteDTO> listCommits = new ArrayList<>();
        // For each repository
        for (HashMap<String, String> repoItem : repoData) {
            log.info("Beginning for repository : {}", repoItem.get(Util.NAME));

            JSONObject commitData = commitService.getCommitData(repoItem.get(Util.OWNER), repoItem.get(Util.NAME), repoItem.get(Util.SHA));

            // Preparation of the Commit data to be inserted
            CommitCompleteDTO commitCompleteDTO = commitService.createCommitEntityDTO(repoItem, commitData);
            listCommits.add(commitCompleteDTO);

            // Insertion of CK data
            String csvPath = System.getenv("REPO_DIRECTORY") + repoItem.get(Util.NAME) + "/output-ck/";
            ckService.insertBatchCkEntityDTO(commitCompleteDTO, csvPath, Integer.parseInt(System.getenv("BATCH_SIZE")));

            // Insertion of Joular data
            CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO = ckService.aggregate(repoItem.get(Util.NAME));
            List<File> iterationPaths = Util.searchDirectories("joularjx-result", new File(System.getenv("REPO_DIRECTORY") + repoItem.get(Util.NAME)));

            for (File iterationFilePath : iterationPaths) {
                String iterationPath = iterationFilePath.getAbsolutePath();
                JoularEntityListDTO joularEntityDTOList = joularServiceImpl.createJoularEntityDTOList(ckAggregateLineHashMapDTO, commitCompleteDTO, iterationPath);
                insertJoularData(joularEntityDTOList);
            }

            log.info("Ending for the repository: {}", repoItem.get(Util.NAME));
        }
        insertCommits(listCommits);

        long totalTime = System.currentTimeMillis() - startTime;
        String lineToAdd = "Insertion of all the data into the database: " + totalTime/1000 + " seconds\n";
        Util.writeTimeToFile(lineToAdd);
    }

    public void setRepoData() {
        HashMap<String, String> commonsConfiguration = new HashMap<>();
        commonsConfiguration.put(Util.OWNER, "apache");
        commonsConfiguration.put(Util.NAME, "commons-configuration");
        commonsConfiguration.put(Util.SHA, "59e5152722198526c6ffe5361de7d1a6a87275c7");

        HashMap<String, String> springBoot = new HashMap<>();
        springBoot.put(Util.OWNER, "spring-projects");
        springBoot.put(Util.NAME, "spring-boot");
        springBoot.put(Util.SHA, "3ed1f1a064a10e53adc2ad8c0b46a4b2c148ee21");

        HashMap<String, String> hibernate = new HashMap<>();
        hibernate.put(Util.OWNER, "hibernate");
        hibernate.put(Util.NAME, "hibernate-orm");
        hibernate.put(Util.SHA, "12442bd8c7cde6e7c006a6277eeb8e81ad0c2219");

        HashMap<String, String> jabref = new HashMap<>();
        jabref.put(Util.OWNER, "JabRef");
        jabref.put(Util.NAME, "jabref");
        jabref.put(Util.SHA, "5c9d8989f968d0ee3a942b411ef7fe121ed94609");

        HashMap<String, String> spoon = new HashMap<>();
        spoon.put(Util.OWNER, "INRIA");
        spoon.put(Util.NAME, "spoon");
        spoon.put(Util.SHA, "066f4cf207359e06d30911a553dedd054aef595c");

        this.repoData.add(commonsConfiguration);
        this.repoData.add(springBoot);
        this.repoData.add(hibernate);
        this.repoData.add(jabref);
        this.repoData.add(spoon);
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
