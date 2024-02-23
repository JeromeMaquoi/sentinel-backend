package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.JoularNodeEntity;
import com.snail.sentinel.backend.repository.JoularNodeEntityRepository;
import com.snail.sentinel.backend.service.JoularNodeEntityService;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.service.mapper.JoularNodeEntityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.snail.sentinel.backend.domain.JoularNodeEntity}.
 */
@Service
public class JoularNodeEntityServiceImpl implements JoularNodeEntityService {

    private final Logger log = LoggerFactory.getLogger(JoularNodeEntityServiceImpl.class);

    private final JoularNodeEntityRepository joularNodeEntityRepository;

    private final JoularNodeEntityMapper joularNodeEntityMapper;

    public JoularNodeEntityServiceImpl(
        JoularNodeEntityRepository joularNodeEntityRepository,
        JoularNodeEntityMapper joularNodeEntityMapper
    ) {
        this.joularNodeEntityRepository = joularNodeEntityRepository;
        this.joularNodeEntityMapper = joularNodeEntityMapper;
    }

    @Override
    public JoularNodeEntityDTO save(JoularNodeEntityDTO joularNodeEntityDTO) {
        log.debug("Request to save JoularNodeEntity : {}", joularNodeEntityDTO);
        JoularNodeEntity joularNodeEntity = joularNodeEntityMapper.toEntity(joularNodeEntityDTO);
        joularNodeEntity = joularNodeEntityRepository.save(joularNodeEntity);
        return joularNodeEntityMapper.toDto(joularNodeEntity);
    }

    @Override
    public JoularNodeEntityDTO update(JoularNodeEntityDTO joularNodeEntityDTO) {
        log.debug("Request to update JoularNodeEntity : {}", joularNodeEntityDTO);
        JoularNodeEntity joularNodeEntity = joularNodeEntityMapper.toEntity(joularNodeEntityDTO);
        joularNodeEntity = joularNodeEntityRepository.save(joularNodeEntity);
        return joularNodeEntityMapper.toDto(joularNodeEntity);
    }

    @Override
    public Optional<JoularNodeEntityDTO> partialUpdate(JoularNodeEntityDTO joularNodeEntityDTO) {
        log.debug("Request to partially update JoularNodeEntity : {}", joularNodeEntityDTO);

        return joularNodeEntityRepository
            .findById(joularNodeEntityDTO.getId())
            .map(existingJoularNodeEntity -> {
                joularNodeEntityMapper.partialUpdate(existingJoularNodeEntity, joularNodeEntityDTO);

                return existingJoularNodeEntity;
            })
            .map(joularNodeEntityRepository::save)
            .map(joularNodeEntityMapper::toDto);
    }

    @Override
    public List<JoularNodeEntityDTO> findAll() {
        log.debug("Request to get all JoularNodeEntities");
        return joularNodeEntityRepository
            .findAll()
            .stream()
            .map(joularNodeEntityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<JoularNodeEntityDTO> findOne(String id) {
        log.debug("Request to get JoularNodeEntity : {}", id);
        return joularNodeEntityRepository.findById(id).map(joularNodeEntityMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete JoularNodeEntity : {}", id);
        joularNodeEntityRepository.deleteById(id);
    }
}
