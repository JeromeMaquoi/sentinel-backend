package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.service.CkEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class CkEntityResource {
    private final Logger log = LoggerFactory.getLogger(CkEntityResource.class);

    private static final String ENTITY_NAME = "ckEntity";

    private final CkEntityRepository ckEntityRepository;

    private final CkEntityService ckService;

    public CkEntityResource(CkEntityRepository ckEntityRepository, CkEntityService ckService) {
        this.ckEntityRepository = ckEntityRepository;
        this.ckService = ckService;
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

    @GetMapping("/ck-entities/by-commit/{sha}")
    public ResponseEntity<Page<CkEntity>> getAllCkEntitiesByCommitSha(
        @PathVariable String sha,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size
    ) {
        log.debug("REST request to get all ck entities data from commit : {}", sha);
        List<CkEntity> ckEntities = ckService.findByCommitSha(sha);
        return getPageResponseEntity(page, size, ckEntities);
    }

    @GetMapping("/ck-entities/by-commit-and-ast-elem/{sha}")
    public List<CkEntity> getSeveralCkEntitiesForOneMethodFromOneCommit(
        @PathVariable String sha,
        @RequestParam String className,
        @RequestParam String methodSignature,
        @RequestParam List<String> names
    ) {
        log.debug("REST request to get all ck entities data from commit : {}", sha);
        return ckService.findByCommitShaAndMethodElementAndMetricNames(sha, className, methodSignature, names);
    }

    /*@GetMapping("/ck-entities/by-commit-and-metric/{sha}")
    public ResponseEntity<Page<CkEntity>> getAllCkEntitiesByCommitShaAndMetricName(
        @PathVariable String sha,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size,
        @RequestParam String metricName
    ) {
        log.debug("REST request to get all ck entities data from commit : {}", sha);
        List<CkEntity> ckEntities = ckService.findByCommitShaAndMetricName(sha, metricName);
        return getPageResponseEntity(page, size, ckEntities);
    }*/

    private ResponseEntity<Page<CkEntity>> getPageResponseEntity(int page, int size, List<CkEntity> ckEntities) {
        if (!ckEntities.isEmpty()) {
            int totalSize = ckEntities.size();
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalSize);

            List<CkEntity> ckContent = ckEntities.subList(startIndex, endIndex);
            Page<CkEntity> pageContent = new PageImpl<>(ckContent, PageRequest.of(page, size), totalSize);
            return new ResponseEntity<>(pageContent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
