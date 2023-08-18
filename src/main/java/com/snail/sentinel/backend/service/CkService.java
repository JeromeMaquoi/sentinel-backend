package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CkService {
    private final Logger log = LoggerFactory.getLogger(CkService.class);

    private final CkEntityRepository ckEntityRepository;

    public CkService(CkEntityRepository ckEntityRepository) {
        this.ckEntityRepository = ckEntityRepository;
    }

    public List<CkEntity> findAll() {
        return ckEntityRepository.findAll();
    }

    public List<CkEntity> bulkAdd(List<CkEntityDTO> listCk) {
        return ckEntityRepository.insert(listCk.stream().map(this::buildNewCkItem).toList());
    }

    public void deleteAll() {
        ckEntityRepository.deleteAll();
    }

    public CkEntity buildNewCkItem(CkEntityDTO ckEntityDTO) {
        CkEntity newCk = new CkEntity();
        newCk.setName(ckEntityDTO.getName());
        newCk.setValue(ckEntityDTO.getValue());
        newCk.setToolVersion(ckEntityDTO.getToolVersion());
        newCk.setCommit(ckEntityDTO.getCommit());
        newCk.setMeasurableElement(ckEntityDTO.getMeasurableElementDTO());
        log.debug("Created Information for Ck: {}", newCk);
        return newCk;
    }
}
