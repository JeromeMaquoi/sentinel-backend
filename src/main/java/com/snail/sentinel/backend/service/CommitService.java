package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
import com.snail.sentinel.backend.service.dto.CommitEntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommitService {
    private final Logger log = LoggerFactory.getLogger(CommitService.class);

    private final CommitEntityRepository commitEntityRepository;

    public CommitService(CommitEntityRepository commitEntityRepository) {
        this.commitEntityRepository = commitEntityRepository;
    }

    public CommitEntity add(CommitEntityDTO commitEntityDTO) {
        CommitEntity commitEntity = new CommitEntity();
        commitEntity.setSha(commitEntityDTO.getSha());
        commitEntity.setRepository(commitEntityDTO.getRepositoryDTO());
        log.debug("Created Information for Commit: {}", commitEntity);
        return commitEntityRepository.save(commitEntity);
    }
}
