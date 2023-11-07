package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
    public Page<CkEntity> getAllCkEntities(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size
    ) {
        log.debug("REST request to get all CkEntities");
        PageRequest pageRequest = PageRequest.of(page, size);
        return ckEntityRepository.findAll(pageRequest);
    }
}
