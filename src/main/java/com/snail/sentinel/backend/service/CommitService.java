package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.commit.StatsDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositoryCompleteDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
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
    private static final Logger log = LoggerFactory.getLogger(CommitService.class);

    private final CommitEntityRepository commitEntityRepository;

    public CommitService(CommitEntityRepository commitEntityRepository) {
        this.commitEntityRepository = commitEntityRepository;
    }

    public CommitEntity add(CommitCompleteDTO commitCompleteDTO) {
        CommitEntity commitEntity = buildNewCommitItem(commitCompleteDTO);
        log.debug("Created Information for Commit: {}", commitEntity);
        return commitEntityRepository.save(commitEntity);
    }

    public Optional<CommitEntity> findOneBySha(String sha) {
        return commitEntityRepository.findOneBySha(sha);
    }

    public List<CommitEntity> bulkAdd(List<CommitCompleteDTO> listCommitDTO) {
        return commitEntityRepository.insert(listCommitDTO.stream().map(this::buildNewCommitItem).toList());
    }

    public void deleteAll() {
        commitEntityRepository.deleteAll();
    }

    private CommitEntity buildNewCommitItem(CommitCompleteDTO commitCompleteDTO) {
        CommitEntity commit = new CommitEntity();
        commit.setSha(commitCompleteDTO.getSha());
        commit.setDate(commitCompleteDTO.getDate());
        commit.setMessage(commitCompleteDTO.getMessage());
        commit.setParentsSha(commitCompleteDTO.getParentsSha());
        commit.setRepository(commitCompleteDTO.getRepository());
        commit.setStats(commitCompleteDTO.getStatsDTO());
        return commit;
    }

    public CommitCompleteDTO createCommitEntityDTO(Map<String, String> repoItem, JSONObject commitData) {
        RepositoryCompleteDTO repositoryCompleteDTO = new RepositoryCompleteDTO();
        repositoryCompleteDTO.setName(repoItem.get(Util.NAME));
        repositoryCompleteDTO.setOwner(repoItem.get(Util.OWNER));
        repositoryCompleteDTO.setUrl(commitData.getString("html_url").split("/commit")[0]);

        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setAdditions(parseInt(commitData.getJSONObject("stats").get("additions").toString()));
        statsDTO.setDeletions(parseInt(commitData.getJSONObject("stats").get("deletions").toString()));

        CommitCompleteDTO commitCompleteDTO = new CommitCompleteDTO();
        commitCompleteDTO.setSha(repoItem.get(Util.SHA));
        commitCompleteDTO.setDate(commitData.getJSONObject("commit").getJSONObject("committer").getString("date"));
        commitCompleteDTO.setMessage(commitData.getJSONObject("commit").getString("message"));
        commitCompleteDTO.setParentsSha(createParentsList(commitData.getJSONArray("parents")));
        commitCompleteDTO.setRepository(repositoryCompleteDTO);
        commitCompleteDTO.setStatsDTO(statsDTO);
        return commitCompleteDTO;
    }

    private List<String> createParentsList(JSONArray parents) {
        return IntStream.range(0, parents.length())
            .mapToObj(index -> ((JSONObject)parents.get(index)).optString(Util.SHA))
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
        if (response.statusCode() == 401) {
            throw new BadCredentialsException("Bad credentials for GitHub !");
        }
        return response.body();
    }
}
