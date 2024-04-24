package com.snail.sentinel.backend;

import com.snail.sentinel.backend.commons.ProductionFileListProvider;
import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.service.*;
import com.snail.sentinel.backend.service.dto.RepoDataDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CkJoularResource {
    private final Logger log = LoggerFactory.getLogger(CkJoularResource.class);

    private List<RepoDataDTO> repoDataList;

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
        this.repoDataList = new ArrayList<>();
    }

    public void insertAllData() throws Exception {
        Util.writeTimeToFile("Insertion of all the data into the db");

        //ckEntityService.deleteAll();
        joularEntityService.deleteAll();
        joularNodeEntityService.deleteAll();

        String projectsDataPath = System.getenv("REPO_DIRECTORY") + "projects-data.csv";
        repoDataList = new ProjectDataReader().readProjectsFromCSV(projectsDataPath);
        List<CommitCompleteDTO> listCommits = new ArrayList<>();
        // For each repository
        for (RepoDataDTO repoData : repoDataList) {
            log.info("Beginning for repository : {}", repoData.getName());

            JSONObject commitData = commitEntityService.getCommitData(repoData.getOwner(), repoData.getName(), repoData.getSha());

            // Preparation of the Commit data to be inserted
            CommitCompleteDTO commitCompleteDTO = commitEntityService.createCommitEntityDTO(repoData, commitData);
            listCommits.add(commitCompleteDTO);

            // Insertion of CK data
            String csvPath = System.getenv("REPO_DIRECTORY") + repoData.getName() + "/output-ck/";
            //ckEntityService.insertBatchCkEntityDTO(commitCompleteDTO, csvPath, Integer.parseInt(System.getenv("BATCH_SIZE")));

            // Insertion of Joular data
            joularResourceService.setFileListProvider(new ProductionFileListProvider());
            joularService.insertBatchJoularData(repoData, commitData);
            log.info("Ending for the repository: {}", repoData.getName());
        }
        insertCommits(listCommits);

        Util.writeTimeToFile("All data inserted to the database");
    }

    public void insertCommits(List<CommitCompleteDTO> listCommits) {
        commitEntityService.deleteAll();
        commitEntityService.bulkAdd(listCommits);
        log.info("Created information for the list of CommitEntity");
    }
}
