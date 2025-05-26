package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.AstElemEntity;
import com.snail.sentinel.backend.repository.AstElemEntityRepository;
import com.snail.sentinel.backend.service.AstElemEntityService;
import com.snail.sentinel.backend.service.dto.AstElemEntityDTO;
import com.snail.sentinel.backend.service.mapper.AstElemEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AstElemEntityServiceImpl implements AstElemEntityService {
    private final Logger log = LoggerFactory.getLogger(AstElemEntityServiceImpl.class);
    private final AstElemEntityRepository astElemEntityRepository;
    private final AstElemEntityMapper astElemEntityMapper;

    public AstElemEntityServiceImpl(AstElemEntityRepository astElemEntityRepository, AstElemEntityMapper astElemEntityMapper) {
        this.astElemEntityRepository = astElemEntityRepository;
        this.astElemEntityMapper = astElemEntityMapper;
    }

    @Override
    public AstElemEntityDTO save(AstElemEntityDTO astElemEntityDTO) {
        log.debug("Request to save AstElemEntity : {}", astElemEntityDTO);
        AstElemEntity astElemEntity = astElemEntityMapper.toEntity(astElemEntityDTO);
        astElemEntity = astElemEntityRepository.save(astElemEntity);
        return astElemEntityMapper.toDto(astElemEntity);
    }

    @Override
    public List<AstElemEntityDTO> bulkAdd(List<AstElemEntityDTO> astElemEntityDTOList) {
        log.debug("Request to save list of AstElemEntity : {}", astElemEntityDTOList);
        List<AstElemEntity> astElemEntity = astElemEntityMapper.toEntity(astElemEntityDTOList);
        astElemEntity = astElemEntityRepository.insert(astElemEntity);
        return astElemEntityMapper.toDto(astElemEntity);
    }

    @Override
    public Optional<AstElemEntityDTO> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<AstElemEntityDTO> findAll() {
        return List.of();
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void deleteAll() {

    }
}
