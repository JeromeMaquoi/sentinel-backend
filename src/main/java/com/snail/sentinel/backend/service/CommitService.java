package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
import com.snail.sentinel.backend.service.dto.commit.CommitEntityDTO;
import com.snail.sentinel.backend.service.dto.commit.StatsDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositoryCompleteDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

@Service
public class CommitService {
    private final Logger log = LoggerFactory.getLogger(CommitService.class);

    private final CommitEntityRepository commitEntityRepository;

    public CommitService(CommitEntityRepository commitEntityRepository) {
        this.commitEntityRepository = commitEntityRepository;
    }

    public CommitEntity add(CommitEntityDTO commitEntityDTO) {
        CommitEntity commitEntity = buildNewCommitItem(commitEntityDTO);
        log.debug("Created Information for Commit: {}", commitEntity);
        return commitEntityRepository.save(commitEntity);
    }

    public Optional<CommitEntity> findOneBySha(String sha) {
        return commitEntityRepository.findOneBySha(sha);
    }

    public List<CommitEntity> bulkAdd(List<CommitEntityDTO> listCommitDTO) {
        return commitEntityRepository.insert(listCommitDTO.stream().map(this::buildNewCommitItem).toList());
    }

    public void deleteAll() {
        commitEntityRepository.deleteAll();
    }

    private CommitEntity buildNewCommitItem(CommitEntityDTO commitEntityDTO) {
        CommitEntity commit = new CommitEntity();
        commit.setSha(commitEntityDTO.getSha());
        commit.setDate(commitEntityDTO.getDate());
        commit.setMessage(commitEntityDTO.getMessage());
        commit.setParentsSha(commitEntityDTO.getParentsSha());
        commit.setRepository(commitEntityDTO.getRepositoryCompleteDTO());
        commit.setStats(commitEntityDTO.getStatsDTO());
        return commit;
    }

    public CommitEntityDTO createDTO(Map<String, String> item, JSONObject jsonData) {
        RepositoryCompleteDTO repositoryCompleteDTO = new RepositoryCompleteDTO();
        repositoryCompleteDTO.setName(item.get("name"));
        repositoryCompleteDTO.setOwner(item.get("owner"));
        repositoryCompleteDTO.setUrl(jsonData.getString("html_url").split("/commit")[0]);

        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setAdditions(parseInt(jsonData.getJSONObject("stats").get("additions").toString()));
        statsDTO.setDeletions(parseInt(jsonData.getJSONObject("stats").get("deletions").toString()));

        CommitEntityDTO commitEntityDTO = new CommitEntityDTO();
        commitEntityDTO.setSha(item.get("sha"));
        commitEntityDTO.setDate(jsonData.getJSONObject("commit").getJSONObject("committer").getString("date"));
        commitEntityDTO.setMessage(jsonData.getJSONObject("commit").getString("message"));
        commitEntityDTO.setParentsSha(createParentsList(jsonData.getJSONArray("parents")));
        commitEntityDTO.setRepositoryCompleteDTO(repositoryCompleteDTO);
        commitEntityDTO.setStatsDTO(statsDTO);
        return commitEntityDTO;
    }

    private List<String> createParentsList(JSONArray parents) {
        return IntStream.range(0, parents.length())
            .mapToObj(index -> ((JSONObject)parents.get(index)).optString("sha"))
            .collect(Collectors.toList());
    }

    public JSONObject getCommitData(String owner, String repoName, String sha) throws Exception {
        String bearer = System.getenv("GITHUB_TOKEN");
        String strUrl = "https://api.github.com/repos/" + owner + "/" + repoName + "/commits/" + sha;
        String response = getBufferedReader(strUrl, bearer);
        return new JSONObject(response);
    }

    private static String getBufferedReader(String strUrl, String bearer) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(strUrl))
            .header("Accept", "application/vnd.github+json")
            .header("Authorization", "Bearer " + bearer)
            .header("X-GitHub-Api-Version", "2022-11-28")
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
