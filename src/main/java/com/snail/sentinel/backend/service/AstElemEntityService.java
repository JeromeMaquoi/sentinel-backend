package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.AstElemEntityDTO;

import java.util.List;
import java.util.Optional;

public interface AstElemEntityService {
    AstElemEntityDTO save(AstElemEntityDTO astElemEntity);
    List<AstElemEntityDTO> bulkAdd(List<AstElemEntityDTO> astElemEntityDTOList);
    Optional<AstElemEntityDTO> findById(String id);
    List<AstElemEntityDTO> findAll();
    void delete(String id);
    void deleteAll();
}
