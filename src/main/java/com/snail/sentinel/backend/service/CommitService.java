package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
import com.snail.sentinel.backend.service.dto.CommitEntityDTO;
import com.snail.sentinel.backend.service.dto.RepositoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        RepositoryDTO repository = new RepositoryDTO();
        repository.setName(commitEntityDTO.getRepoName());
        repository.setOwner(commitEntityDTO.getOwner());

        CommitEntity commit = new CommitEntity();
        commit.setSha(commitEntityDTO.getSha());
        commit.setRepository(repository);

        return commit;
    }
}
