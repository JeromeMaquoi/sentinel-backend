package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.service.CkEntityService;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import com.snail.sentinel.backend.service.mapper.CkEntityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link CkEntity}.
 */
@Service
public class CkEntityServiceImpl implements CkEntityService {

    private final Logger log = LoggerFactory.getLogger(CkEntityServiceImpl.class);

    private final CkEntityRepository ckEntityRepository;

    private final CkEntityMapper ckEntityMapper;

    public CkEntityServiceImpl(CkEntityRepository ckEntityRepository, CkEntityMapper ckEntityMapper) {
        this.ckEntityRepository = ckEntityRepository;
        this.ckEntityMapper = ckEntityMapper;
    }

    @Override
    public CkEntityDTO save(CkEntityDTO ckEntityDTO) {
        log.debug("Request to save CkEntity : {}", ckEntityDTO);
        CkEntity ckEntity = ckEntityMapper.toEntity(ckEntityDTO);
        ckEntity = ckEntityRepository.save(ckEntity);
        return ckEntityMapper.toDto(ckEntity);
    }

    @Override
    public CkEntityDTO update(CkEntityDTO ckEntityDTO) {
        log.debug("Request to update CkEntity : {}", ckEntityDTO);
        CkEntity ckEntity = ckEntityMapper.toEntity(ckEntityDTO);
        ckEntity = ckEntityRepository.save(ckEntity);
        return ckEntityMapper.toDto(ckEntity);
    }

    @Override
    public Optional<CkEntityDTO> partialUpdate(CkEntityDTO ckEntityDTO) {
        log.debug("Request to partially update CkEntity : {}", ckEntityDTO);

        return ckEntityRepository
            .findById(ckEntityDTO.getId())
            .map(existingCkEntity -> {
                ckEntityMapper.partialUpdate(existingCkEntity, ckEntityDTO);

                return existingCkEntity;
            })
            .map(ckEntityRepository::save)
            .map(ckEntityMapper::toDto);
    }

    @Override
    public List<CkEntityDTO> findAll() {
        log.debug("Request to get all CkEntities");
        return ckEntityRepository.findAll().stream().map(ckEntityMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<CkEntityDTO> findOne(String id) {
        log.debug("Request to get CkEntity : {}", id);
        return ckEntityRepository.findById(id).map(ckEntityMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete CkEntity : {}", id);
        ckEntityRepository.deleteById(id);
    }
}
