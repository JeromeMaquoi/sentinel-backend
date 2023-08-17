package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CkService {
    private final Logger log = LoggerFactory.getLogger(CkService.class);

    private final CkEntityRepository ckEntityRepository;

    private final List<CkEntity> listCk;

    public CkService(CkEntityRepository ckEntityRepository) {
        this.ckEntityRepository = ckEntityRepository;
        this.listCk = new ArrayList<>();
    }

    public void insertToList(CkEntityDTO ckEntityDTO) {
        CkEntity newCk = new CkEntity();
        newCk.setName(ckEntityDTO.getName());
        newCk.setValue(ckEntityDTO.getValue());
        newCk.setToolVersion(ckEntityDTO.getToolVersion());
        newCk.setCommit(ckEntityDTO.getCommit());
        newCk.setMeasurableElement(ckEntityDTO.getMeasurableElementDTO());
        this.listCk.add(newCk);
        log.debug("Created Information for Ck: {}", newCk);
    }

    public List<CkEntity> save() {
        ckEntityRepository.insert(this.listCk);
        log.debug("Created Information for list of Ck: {}", this.listCk);
        return this.listCk;
    }
}
