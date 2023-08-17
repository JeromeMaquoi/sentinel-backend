package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CkService {
    private final Logger log = LoggerFactory.getLogger(CkService.class);

    private final CkEntityRepository ckEntityRepository;

    public CkService(CkEntityRepository ckEntityRepository) {
        this.ckEntityRepository = ckEntityRepository;
    }

    public CkEntity createCk(CkEntityDTO ckEntityDTO) {
        CkEntity newCk = new CkEntity();
        newCk.setName(ckEntityDTO.getName());
        newCk.setValue(ckEntityDTO.getValue());
        newCk.setToolVersion(ckEntityDTO.getToolVersion());
        newCk.setCommit(ckEntityDTO.getCommit());
        newCk.setMeasurableElement(ckEntityDTO.getMeasurableElementDTO());
        ckEntityRepository.save(newCk);
        log.debug("Created Information for Ck: {}", newCk);
        return newCk;
    }
}
