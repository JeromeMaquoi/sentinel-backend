package com.snail.sentinel.backend;

import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
import com.snail.sentinel.backend.service.CkService;
import com.snail.sentinel.backend.service.CommitService;
import com.snail.sentinel.backend.service.dto.commit.CommitEntityDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    public void insertAllData() throws IOException, InterruptedException {
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

    public void insertCommits() throws IOException, InterruptedException {
        commitService.deleteAll();
        commitService.bulkAdd(createCommitList());
    }

    public List<CommitEntityDTO> createCommitList() throws IOException, InterruptedException {
        List<CommitEntityDTO> newList = new ArrayList<>();
        for (HashMap<String, String> item : repoData) {
            CommitEntityDTO commitEntityDTO = new CommitEntityDTO();
            commitEntityDTO.setSha(item.get("sha"));
            newList.add(commitEntityDTO);
            getCommitData(item.get("owner"), item.get("repo"), item.get("sha"));
        }
        return newList;
    }

    private void getCommitData(String owner, String repoName, String sha) throws IOException, InterruptedException {
        String bearer = "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";
        String strUrl = "https://api.github.com/repos/" + owner + "/" + repoName + "/commits/" + sha;
        getBufferedReader(strUrl, bearer);
    }

    private static void getBufferedReader(String strUrl, String bearer) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(strUrl))
            .header("Accept", "application/vnd.github+json")
            .header("Authorization", "Bearer " + bearer)
            .header("X-GitHub-Api-Version", "2022-11-28")
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
