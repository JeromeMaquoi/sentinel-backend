package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CkEntityResource {
    private final Logger log = LoggerFactory.getLogger(CkEntityResource.class);

    private static final String ENTITY_NAME = "ckEntity";

    private final CkEntityRepository ckEntityRepository;

    public CkEntityResource(CkEntityRepository ckEntityRepository) {
        this.ckEntityRepository = ckEntityRepository;
    }

    @GetMapping("/ck-entities")
    public List<CkEntity> getAllCkEntities() {
        log.debug("REST request to get all CkEntities");
        return ckEntityRepository.findAll();
    }
}
