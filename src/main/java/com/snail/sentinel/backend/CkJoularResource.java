package com.snail.sentinel.backend;

import com.snail.sentinel.backend.commons.ProductionFileListProvider;
import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.service.*;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CkJoularResource {
    private final Logger log = LoggerFactory.getLogger(CkJoularResource.class);

    private final List<HashMap<String, String>> repoData;

    private final CkEntityService ckEntityService;

    private final CommitEntityService commitEntityService;

    private final JoularService joularService;

    private final JoularEntityService joularEntityService;

    private final JoularNodeEntityService joularNodeEntityService;

    private final JoularResourceService joularResourceService;

    public CkJoularResource(CkEntityService ckEntityService, CommitEntityService commitEntityService, JoularService joularService, JoularEntityService joularEntityService, JoularNodeEntityService joularNodeEntityService, JoularResourceService joularResourceService) {
        this.ckEntityService = ckEntityService;
        this.commitEntityService = commitEntityService;
        this.joularService = joularService;
        this.joularEntityService = joularEntityService;
        this.joularNodeEntityService = joularNodeEntityService;
        this.joularResourceService = joularResourceService;
        this.repoData = new ArrayList<>();
    }

    public void insertAllData() throws Exception {
        Util.writeTimeToFile("Insertion of all the data into the db");

        //ckEntityService.deleteAll();
        //joularEntityService.deleteAll();
        joularNodeEntityService.deleteAll();
        setRepoData();
        List<CommitCompleteDTO> listCommits = new ArrayList<>();
        // For each repository
        for (HashMap<String, String> repoItem : repoData) {
            log.info("Beginning for repository : {}", repoItem.get(Util.NAME));

            JSONObject commitData = commitEntityService.getCommitData(repoItem.get(Util.OWNER), repoItem.get(Util.NAME), repoItem.get(Util.SHA));

            // Preparation of the Commit data to be inserted
            CommitCompleteDTO commitCompleteDTO = commitEntityService.createCommitEntityDTO(repoItem, commitData);
            listCommits.add(commitCompleteDTO);

            // Insertion of CK data
            String csvPath = System.getenv("REPO_DIRECTORY") + repoItem.get(Util.NAME) + "/output-ck/";
            //ckEntityService.insertBatchCkEntityDTO(commitCompleteDTO, csvPath, Integer.parseInt(System.getenv("BATCH_SIZE")));

            // Insertion of Joular data
            joularResourceService.setFileListProvider(new ProductionFileListProvider());
            joularService.insertBatchJoularData(repoItem, commitData);
            log.info("Ending for the repository: {}", repoItem.get(Util.NAME));
        }
        insertCommits(listCommits);

        Util.writeTimeToFile("All data inserted to the database");
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

        //this.repoData.add(commonsConfiguration);
        //this.repoData.add(springBoot);
        //this.repoData.add(hibernate);
        this.repoData.add(jabref);
        //this.repoData.add(spoon);
    }

    public void insertCommits(List<CommitCompleteDTO> listCommits) {
        commitEntityService.deleteAll();
        commitEntityService.bulkAdd(listCommits);
        log.info("Created information for the list of CommitEntity");
    }

    /*public void insertJoularData(JoularEntityListDTO joularEntityListDTO) {
        joularService.bulkAdd(joularEntityListDTO.getList());
        log.info("List of JoularEntity inserted to the database");
    }*/
}
